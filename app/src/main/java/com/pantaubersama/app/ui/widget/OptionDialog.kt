package com.pantaubersama.app.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.KeyEvent
import android.view.WindowManager
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes

class OptionDialog<T>(context: Context, item: T, @LayoutRes optionLayout: Int) {

    private val dialog: Dialog
    private val dialogLayout: ViewGroup

    var listener: OptionDialog.DialogListener? = null

    init {
        val dialog = Dialog(context)
        val dialogLayout = LayoutInflater.from(context).inflate(optionLayout, null)
        dialog.setContentView(dialogLayout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { dialogInterface, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else {
                false
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.BOTTOM
        window?.attributes = lp
        this.dialog = dialog
        this.dialogLayout = dialogLayout as ViewGroup
    }

    fun show() {
        for (i in 0..(dialogLayout.childCount - 1)) {
            dialogLayout.getChildAt(i).setOnClickListener {
                listener?.onClick(dialogLayout.getChildAt(i).id)
            }
        }
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun removeItem(viewId: Int) {
        val itemView = dialog.findViewById<View>(viewId)
        if (itemView != null) {
            itemView.visibility = View.GONE
        }
    }

    interface DialogListener {
        fun onClick(position: Int)
    }
}