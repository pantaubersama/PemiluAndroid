package com.pantaubersama.app.utils

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R

class CustomClickableSpan(private val context: Context?, private val text: String?, private val textType: Int?, val callback: Callback?) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint?) {
        ds?.color = ContextCompat.getColor(context!!, R.color.colorPrimary)
        ds?.isUnderlineText = false
    }

    override fun onClick(widget: View?) {
        when (textType) {
            0 -> callback?.onClickUrl(text)
            1 -> callback?.onClickMention(text?.substring(1))
        }
    }

    interface Callback {
        fun onClickUrl(url: String?)
        fun onClickMention(mention: String?)
    }
}