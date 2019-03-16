package com.pantaubersama.app.ui.debat

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.spannable

/**
 * @author edityomurti on 16/03/2019 17:26
 */
class TolakChallengeDialog(
    context: Context,
    challengerUsername: String?,
    listener: DialogListener
) {
    private val dialog: Dialog

    init {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_dialog_tolak_challenge)
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
        dialog.findViewById<TextView>(R.id.tv_desc).text = spannable {
            + "Kamu akan menolak Direct Challenge dari "
            textColor(context.color(R.color.black)) { +"@$challengerUsername " }
            + "untuk berdebat. Tulis pernyataan atau alasannya sebagai hak jawab penolakan kamu."
        }.toCharSequence()
        val etRejectReason = dialog.findViewById<EditText>(R.id.et_reject_reason)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btn_tolak)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btn_kembali)
        btnPositive.setOnClickListener {
            if (etRejectReason.text.toString().isEmpty() || etRejectReason.text.toString().isBlank()) {
                ToastUtil.show(context, "Tulis pernyataan atau alasan dulu yak!")
            } else {
                listener.onClickTolak(etRejectReason.text.toString())
                dialog.dismiss()
            }
        }
        btnNegative.setOnClickListener { dialog.dismiss() }
        this.dialog = dialog
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    interface DialogListener {
        fun onClickTolak(reason: String)
    }
}