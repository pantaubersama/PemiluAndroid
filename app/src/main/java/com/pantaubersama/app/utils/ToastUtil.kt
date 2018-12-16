package com.pantaubersama.app.utils

import android.content.Context
import android.widget.Toast

/**
 * @author edityomurti on 17/12/2018 04:27
 */
class ToastUtil {
    companion object {
        fun show(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}