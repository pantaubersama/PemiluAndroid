package com.pantaubersama.app.ui.debat

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.pantaubersama.app.R
import java.io.Serializable

/**
 * @author edityomurti on 04/03/2019 17:41
 */

class TerimaChallengeDialog(
    context: Context,
    message: CharSequence,
    positiveBtnText: String = "SETUJU",
    listener: DialogListener
) {
    private val dialog: Dialog

    init {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_dialog_terima_challenge)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        dialog.findViewById<TextView>(R.id.tv_desc).text = message
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btn_setuju)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btn_kembali)
        btnPositive.text = positiveBtnText
        btnPositive.setOnClickListener {
            listener.onClickTerima()
            dialog.dismiss() }
        btnNegative.setOnClickListener { dialog.dismiss() }
        this.dialog = dialog
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    interface DialogListener : Serializable {
        fun onClickTerima()
    }
}