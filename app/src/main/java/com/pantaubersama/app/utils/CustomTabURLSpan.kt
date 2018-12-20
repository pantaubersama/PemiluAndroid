package com.pantaubersama.app.utils

import android.content.Context
import android.text.style.URLSpan
import android.view.View

/**
 * Created by alimustofa on 14/03/18.
 */
class CustomTabURLSpan(val context: Context, val url: String) : URLSpan(url) {
    override fun onClick(widget: View) {
        ChromeTabUtil(context).loadUrl(url)
    }
}