package com.pantaubersama.app.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import timber.log.Timber

/**
 * Created by alimustofa on 26/01/18.
 */
class ChromeTabUtil(private val context: Context) {
    private var builder: CustomTabsIntent.Builder? = null
    private var customTabsIntent: CustomTabsIntent? = null

    init {
        builder = CustomTabsIntent.Builder()
        builder?.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        builder?.setShowTitle(true)
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
        if (isChromeAppInstalled()) {
            customTabsIntent?.intent?.setPackage("com.android.chrome")
            loadUrl(url)
        } else {
            loadUrl(url)
        }
    }

    private fun isChromeAppInstalled(): Boolean {
        val pkManager = context.packageManager
        var isInstalled = false
        try {
            val pkgInfo = pkManager?.getPackageInfo("com.android.chrome", 0)
            val getPkgInfo = pkgInfo.toString()

            Timber.d("pkginfo : $pkgInfo")

            if (getPkgInfo.contains("com.android.chrome")) {
                isInstalled = true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

            isInstalled = false
        }
        return isInstalled
    }
}