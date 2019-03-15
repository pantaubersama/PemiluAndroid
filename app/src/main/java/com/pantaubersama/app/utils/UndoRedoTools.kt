package com.pantaubersama.app.utils

import android.content.SharedPreferences
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.widget.TextView

import java.util.LinkedList

class UndoRedoTools
// =================================================================== //

/**
 * Create a new TextViewUndoRedo and attach it to the specified TextView.
 *
 * @param textView The text view for which the undoParty/redo is implemented.
 */(
     /**
      * The edit text.
      */
     private val mTextView: TextView
 ) {

    /**
     * Is undoParty/redo being performed? This member signals if an undoParty/redo
     * operation is currently being performed. Changes in the text during
     * undoParty/redo are not recorded because it would mess up the undoParty history.
     */
    private var mIsUndoOrRedo = false

    /**
     * The edit history.
     */
    private val mEditHistory: EditHistory

    /**
     * The change listener.
     */
    private val mChangeListener: EditTextChangeListener

    /**
     * Can undoParty be performed?
     */
    val canUndo: Boolean
        get() = mEditHistory.mmPosition > 0

    /**
     * Can redo be performed?
     */
    val canRedo: Boolean
        get() = mEditHistory.mmPosition < mEditHistory.mmHistory.size

    init {
        mEditHistory = EditHistory()
        mChangeListener = EditTextChangeListener()
        mTextView.addTextChangedListener(mChangeListener)
    }

    // =================================================================== //

    /**
     * Disconnect this undoParty/redo from the text view.
     */
    fun disconnect() {
        mTextView.removeTextChangedListener(mChangeListener)
    }

    /**
     * Set the maximum history size. If size is negative, then history size is
     * only limited by the device memory.
     */
    fun setMaxHistorySize(maxHistorySize: Int) {
        mEditHistory.setMaxHistorySize(maxHistorySize)
    }

    /**
     * Clear history.
     */
    fun clearHistory() {
        mEditHistory.clear()
    }

    /**
     * Perform undoParty.
     */
    fun undo() {
        val edit = mEditHistory.previous ?: return

        val text = mTextView.editableText
        val start = edit.mmStart
        val end = start + if (edit.mmAfter != null) edit.mmAfter.length else 0

        mIsUndoOrRedo = true
        text.replace(start, end, edit.mmBefore)
        mIsUndoOrRedo = false

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (o in text.getSpans(0, text.length, UnderlineSpan::class.java)) {
            text.removeSpan(o)
        }

        Selection.setSelection(text, if (edit.mmBefore == null)
            start
        else
            start + edit.mmBefore.length)
    }

    /**
     * Perform redo.
     */
    fun redo() {
        val edit = mEditHistory.next ?: return

        val text = mTextView.editableText
        val start = edit.mmStart
        val end = start + if (edit.mmBefore != null) edit.mmBefore.length else 0

        mIsUndoOrRedo = true
        text.replace(start, end, edit.mmAfter)
        mIsUndoOrRedo = false

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (o in text.getSpans(0, text.length, UnderlineSpan::class.java)) {
            text.removeSpan(o)
        }

        Selection.setSelection(text, if (edit.mmAfter == null)
            start
        else
            start + edit.mmAfter.length)
    }

    /**
     * Store preferences.
     */
    fun storePersistentState(editor: SharedPreferences.Editor, prefix: String) {
        // Store hash code of text in the editor so that we can check if the
        // editor contents has changed.
        editor.putString("$prefix.hash",
            mTextView.text.toString().hashCode().toString())
        editor.putInt("$prefix.maxSize", mEditHistory.mmMaxHistorySize)
        editor.putInt("$prefix.position", mEditHistory.mmPosition)
        editor.putInt("$prefix.size", mEditHistory.mmHistory.size)

        var i = 0
        for (ei in mEditHistory.mmHistory) {
            val pre = "$prefix.$i"

            editor.putInt("$pre.start", ei.mmStart)
            editor.putString("$pre.before", ei.mmBefore.toString())
            editor.putString("$pre.after", ei.mmAfter.toString())

            i++
        }
    }

    /**
     * Restore preferences.
     *
     * @param prefix The preference key prefix used when state was stored.
     * @return did restore succeed? If this is false, the undoParty history will be
     * empty.
     */
    @Throws(IllegalStateException::class)
    fun restorePersistentState(sp: SharedPreferences, prefix: String): Boolean {

        val ok = doRestorePersistentState(sp, prefix)
        if (!ok) {
            mEditHistory.clear()
        }

        return ok
    }

    private fun doRestorePersistentState(sp: SharedPreferences, prefix: String): Boolean {

        val hash = sp.getString("$prefix.hash", null) // No state to be restored.
            ?: return true

        if (Integer.valueOf(hash) != mTextView.text.toString().hashCode()) {
            return false
        }

        mEditHistory.clear()
        mEditHistory.mmMaxHistorySize = sp.getInt("$prefix.maxSize", -1)

        val count = sp.getInt("$prefix.size", -1)
        if (count == -1) {
            return false
        }

        for (i in 0 until count) {
            val pre = "$prefix.$i"

            val start = sp.getInt("$pre.start", -1)
            val before = sp.getString("$pre.before", null)
            val after = sp.getString("$pre.after", null)

            if (start == -1 || before == null || after == null) {
                return false
            }
            mEditHistory.add(EditItem(start, before, after))
        }

        mEditHistory.mmPosition = sp.getInt("$prefix.position", -1)
        return if (mEditHistory.mmPosition == -1) {
            false
        } else true
    }

    // =================================================================== //

    /**
     * Keeps track of all the edit history of a text.
     */
    inner class EditHistory {

        /**
         * The list of edits in chronological order.
         */
        val mmHistory = LinkedList<EditItem>()
        /**
         * The position from which an EditItem will be retrieved when getNext()
         * is called. If getPrevious() has not been called, this has the same
         * value as mmHistory.size().
         */
        var mmPosition = 0
        /**
         * Maximum undoParty history size.
         */
        var mmMaxHistorySize = -1

        /**
         * Traverses the history backward by one position, returns and item at
         * that position.
         */
        val previous: EditItem?
            get() {
                if (mmPosition == 0) {
                    return null
                }
                mmPosition--
                return mmHistory[mmPosition]
            }

        /**
         * Traverses the history forward by one position, returns and item at
         * that position.
         */
        val next: EditItem?
            get() {
                if (mmPosition >= mmHistory.size) {
                    return null
                }

                val item = mmHistory[mmPosition]
                mmPosition++
                return item
            }

        /**
         * Clear history.
         */
        fun clear() {
            mmPosition = 0
            mmHistory.clear()
        }

        /**
         * Adds a new edit operation to the history at the current position. If
         * executed after a call to getPrevious() removes all the future history
         * (elements with positions >= current history position).
         */
        fun add(item: EditItem) {
            while (mmHistory.size > mmPosition) {
                mmHistory.removeLast()
            }
            mmHistory.add(item)
            mmPosition++

            if (mmMaxHistorySize >= 0) {
                trimHistory()
            }
        }

        /**
         * Set the maximum history size. If size is negative, then history size
         * is only limited by the device memory.
         */
        fun setMaxHistorySize(maxHistorySize: Int) {
            mmMaxHistorySize = maxHistorySize
            if (mmMaxHistorySize >= 0) {
                trimHistory()
            }
        }

        /**
         * Trim history when it exceeds max history size.
         */
        private fun trimHistory() {
            while (mmHistory.size > mmMaxHistorySize) {
                mmHistory.removeFirst()
                mmPosition--
            }

            if (mmPosition < 0) {
                mmPosition = 0
            }
        }
    }

    /**
     * Represents the changes performed by a single edit operation.
     */
    inner class EditItem
    /**
     * Constructs EditItem of a modification that was applied at position
     * start and replaced CharSequence before with CharSequence after.
     */
    (val mmStart: Int, val mmBefore: CharSequence?, val mmAfter: CharSequence?)

    /**
     * Class that listens to changes in the text.
     */
    private inner class EditTextChangeListener : TextWatcher {

        /**
         * The text that will be removed by the change event.
         */
        private var mBeforeChange: CharSequence? = null

        /**
         * The text that was inserted by the change event.
         */
        private var mAfterChange: CharSequence? = null

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            if (mIsUndoOrRedo) {
                return
            }

            mBeforeChange = s.subSequence(start, start + count)
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            if (mIsUndoOrRedo) {
                return
            }

            mAfterChange = s.subSequence(start, start + count)
            mEditHistory.add(EditItem(start, mBeforeChange, mAfterChange))
        }

        override fun afterTextChanged(s: Editable) {}
    }
}
