package com.pantaubersama.app.ui.wordstadium

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.pantaubersama.app.R

class InfoDialog : DialogFragment() {
    companion object {
        fun newInstance(layout: Int): InfoDialog {
            val frag = InfoDialog()
            val args = Bundle()
            args.putInt("layout", layout)
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout: Int = arguments!!.getInt("layout")
        val alertDialog = AlertDialog.Builder(activity)
        val view = LayoutInflater.from(activity).inflate(layout, null)
        val btnClose = view.findViewById<View>(R.id.go_it)
        btnClose.setOnClickListener {
            dismiss()
        }
        alertDialog.setView(view)
        return alertDialog.create()
    }
}