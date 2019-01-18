package com.pantaubersama.app.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.google.android.material.button.MaterialButton
import com.pantaubersama.app.R

class UpdateAppDialog(context: Context, listener: DialogListener) {
    private val dialog: Dialog

    init {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_update_app_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        val btnUpdate = dialog.findViewById<MaterialButton>(R.id.btn_update)
        btnUpdate.setOnClickListener { listener.onClickUpdate() }
        this.dialog = dialog
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    interface DialogListener {
        fun onClickUpdate()
    }
}