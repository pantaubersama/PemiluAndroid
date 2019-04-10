package com.pantaubersama.app.ui.debat.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.SortedAdapter
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_OPPONENT
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_OPPONENT
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.drawable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_words_input_challenger.*
import kotlinx.android.synthetic.main.item_words_challenger.*

/**
 * @author edityomurti on 14/02/2019 22:26
 */
@Deprecated("Not yet suitable for SortedList")
class MessageSortedAdapter : SortedAdapter<WordItem>() {

    private lateinit var recyclerView: RecyclerView

    var listener: MessageSortedAdapter.AdapterListener? = null

    override val itemClass: Class<WordItem>
        get() = WordItem::class.java

    override fun compare(item1: WordItem, item2: WordItem): Int {
        return item2.createdAt.compareTo(item1.createdAt)
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        return when (viewType) {
            WORD_TYPE_CHALLENGER -> R.layout.item_words_challenger
            WORD_TYPE_OPPONENT -> R.layout.item_words_opponent
            WORD_INPUT_CHALLENGER -> R.layout.item_words_input_challenger
            WORD_INPUT_OPPONENT -> R.layout.item_words_input_opponent
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data?.get(position)?.getType() ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WORD_TYPE_CHALLENGER -> MessageViewholder(getView(parent, viewType))
            WORD_TYPE_OPPONENT -> MessageViewholder(getView(parent, viewType))
            WORD_INPUT_CHALLENGER -> MessageInputViewHolder(getView(parent, viewType))
            WORD_INPUT_OPPONENT -> MessageInputViewHolder(getView(parent, viewType))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getData(position)?.let {
            (holder as? MessageViewholder)?.bind(it)
            (holder as? MessageInputViewHolder)?.bind(it)
        }
    }

    inner class MessageViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordItem) {
            tv_content.text = item.body
            tv_clap_count.text = item.clapCount.toString()

            if (item.isClap) {
//                iv_clap.setImageResource(R.drawable.ic_clap)
            } else {
//                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            cl_btn_clap.setOnClickListener { onClapClicked(item) }
        }

        fun onClapClicked(item: WordItem) {
            val initialClapState = item.isClap
            item.isClap = !item.isClap
            if (initialClapState) {
                item.clapCount -= 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_pressed_yellow)
            } else {
                item.clapCount += 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            notifyItemChanged(adapterPosition)
        }
    }

    inner class MessageInputViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordItem) {
//            itemView.isEnabled = item.inputState == WordItem.InputState.ACTIVE
//            enableView(item.inputState == WordItem.InputState.ACTIVE, itemView as ViewGroup)

            et_content.setText(item.body)

            et_content.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
                listener?.onMessageInputFocused(isFocused)
            }

            et_content.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (query?.isNotEmpty() == true && query.isNotBlank()) {
//                        if (item.type == WordItem.Type.INPUT_CHALLENGER) {
//                            view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_tosca_2)
//                        } else {
//                            view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_red_2)
//                        }

                        btn_publish.setTextColor(itemView.context.color(R.color.orange_2))

                        if (!btn_publish.isClickable) btn_publish.isClickable = true
                        if (!btn_cancel.isClickable) btn_cancel.isClickable = true

                        if (et_content.lineCount > et_content.minLines) {
                            if (recyclerView.canScrollVertically(1)) {
                                recyclerView.smoothScrollBy(0, recyclerView.bottom - 1)
                            }
                        }
                    } else {
                        view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_2dp_gray_dark_1)

                        btn_publish.setTextColor(itemView.context.color(R.color.gray_dark_1))
                        if (!btn_publish.isClickable) btn_publish.isClickable = false
                        if (!btn_cancel.isClickable) btn_cancel.isClickable = false
                    }
                }
            })

            btn_publish.setOnClickListener {
                val content = et_content.text.toString()
                item.body = content
//                item.inputState = WordItem.InputState.INACTIVE
                et_content.clearFocus()
                notifyItemChanged(adapterPosition)
                listener?.onPublish(content)
            }
        }

        fun enableView(enable: Boolean, viewGroup: ViewGroup) {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                child.isEnabled = enable
                if (child is ViewGroup) {
                    enableView(enable, child)
                }
            }
        }
    }

    interface AdapterListener {
        fun onClickClap()
        fun onMessageInputFocused(isFocused: Boolean)
        fun onPublish(content: String)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

//    fun addMessageInput(type: WordItem.Type) {
//        getData(itemCount - 1)?.let {
//            val inputMessageItem = when (type) {
// //                WordItem.Type.INPUT_CHALLENGER -> WORD_FIGHTER_INPUT_CHALLENGER
//                else -> WORD_FIGHTER_INPUT_OPPONENT
//            }
// //            messageInputItem.createdAt = it.createdAt + 1
// //            addItem(messageInputItem)
//        }
//    }

//    fun clearMe
}