package com.pantaubersama.app.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R

/**
 * Created by alimustofa on 26/01/18.
 */
class ChromeTabUtil(private val context: Context) {
    private var builder: CustomTabsIntent.Builder? = null
    private var customTabsIntent: CustomTabsIntent? = null

    init {
        builder = CustomTabsIntent.Builder()
        builder?.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        customTabsIntent = builder?.build()
    }
    fun loadUrl(url: String?) {
        if (!url!!.startsWith("http")) {
            if (url.startsWith("//")) {
                val trimmedUrl = url.substring(2)
                val newUrl = "http://$trimmedUrl"
                customTabsIntent?.launchUrl(context, Uri.parse(StringUtil.pullUrlFromString(newUrl)))
            } else {
                val newUrl = "http://$url"
                customTabsIntent?.launchUrl(context, Uri.parse(StringUtil.pullUrlFromString(newUrl)))
            }
        } else {
            customTabsIntent?.launchUrl(context, Uri.parse(StringUtil.pullUrlFromString(url)))
        }
    }

    fun forceLoadUrl(url: String?) {
        try {
            customTabsIntent?.intent?.setPackage("com.android.chrome")
            loadUrl(url)
        } catch (e: ActivityNotFoundException) {
            loadUrl(url)
        }
    }
}