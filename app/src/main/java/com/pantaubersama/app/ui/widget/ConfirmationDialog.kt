package com.pantaubersama.app.ui.widget

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.confirmation_dialog.view.*

class ConfirmationDialog {
    class Builder {
        private lateinit var activity: FragmentActivity
        private lateinit var title: String
        private lateinit var alert: String
        private lateinit var okText: String
        private lateinit var cancelText: String
        private lateinit var okListener: DialogOkListener

        fun with(activity: FragmentActivity) = apply {
            this.activity = activity
        }

        fun setDialogTitle(title: String) = apply {
            this.title = title
        }

        fun setAlert(alert: String) = apply {
            this.alert = alert
        }

        fun setOkText(okText: String) = apply {
            this.okText = okText
        }

        fun setCancelText(cancelText: String) = apply {
            this.cancelText = cancelText
        }

        fun addOkListener(okListener: DialogOkListener) = apply {
            this.okListener = okListener
        }

        fun show() {
            val bottomDialog: BottomSheetDialog
            activity.let { bottomDialog = BottomSheetDialog(it) }
            val view: View = activity.layoutInflater.inflate(R.layout.confirmation_dialog, null)
            title.let { view.dialog_title.text = it }
            alert.let { view.dialog_alert.text = it }
            okText.let { view.ok_text.text = it }
            cancelText.let { view.cancel_text.text = it }
            view.dialog_cancel_action.setOnClickListener {
                bottomDialog.dismiss()
            }
            view.dialog_ok_action.setOnClickListener {
                okListener.onClickOk()
                bottomDialog.dismiss()
            }
            bottomDialog.setContentView(view)
            try {
                bottomDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface DialogOkListener {
        fun onClickOk()
    }
}