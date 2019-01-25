package com.pantaubersama.app.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.TextView
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.layout_delete_confirmation_dialog.*

class DeleteConfimationDialog(context: Context, message: String, position: Int = 0, itemId: String = "", listener: DialogListener) {
    private val dialog: Dialog

    init {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_delete_confirmation_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
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
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        dialog.yes_button.setOnClickListener {
            listener.onClickDeleteItem(itemId, position)
            dialog.dismiss()
        }
        dialog.no_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.tv_delete_confirmation_dialog).text = message
        this.dialog = dialog
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    interface DialogListener {
        fun onClickDeleteItem(id: String, position: Int)
    }
}