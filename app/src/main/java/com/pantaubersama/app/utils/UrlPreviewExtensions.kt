package com.pantaubersama.app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.ui.widget.PreviewWebViewClient
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl

/**
 * @author edityomurti on 20/03/2019 17:29
 */

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
fun LinearLayout.previewTweet(html: String) {
    val webview = WebView(context)
    webview.webViewClient = PreviewWebViewClient()
    webview.settings.loadsImagesAutomatically = true
    webview.settings.javaScriptEnabled = true
    webview.settings.setAppCacheEnabled(true)
    webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    webview.loadDataWithBaseURL("https://twitter.com", html, "text/html", "utf-8", "")
    webview.setOnTouchListener { _, _ -> true }
    webview.setBackgroundColor(context.color(android.R.color.transparent))
    addView(webview)
}

fun LinearLayout.previewUrl(urlItem: UrlItem) {
    val urlPreviewLayout = (context as Activity).inflate(R.layout.layout_url_preview)
    addView(urlPreviewLayout)
    findViewById<ImageView>(R.id.iv_url_preview).loadUrl(urlItem.bestImage, R.color.gray_3)
    findViewById<TextView>(R.id.tv_title_url_preview).text = urlItem.bestTitle ?: "..."
    findViewById<TextView>(R.id.tv_description_url_preview).text = urlItem.bestDescriptiom ?: "..."
}
