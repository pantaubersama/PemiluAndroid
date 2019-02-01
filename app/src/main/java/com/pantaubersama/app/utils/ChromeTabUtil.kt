package com.pantaubersama.app.utils

import android.content.Context
import android.content.Intent
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
        url?.let { customTabsIntent?.launchUrl(context, Uri.parse(StringUtil.pullUrlFromString(invalidateUrl(it)))) }
    }

    fun forceLoadUrl(url: String?) {
        if (isChromeAppInstalled()) {
            customTabsIntent?.intent?.setPackage("com.android.chrome")
            loadUrl(url)
        } else {
            loadUrl(url)
        }
    }

    fun getIntent(url: String): Intent? {
        customTabsIntent?.intent?.data = Uri.parse(invalidateUrl(url))
        return customTabsIntent?.intent
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

    private fun invalidateUrl(url: String): String {
        return if (!url.startsWith("http")) {
            if (url.startsWith("//")) {
                val trimmedUrl = url.substring(2)
                val newUrl = "http://$trimmedUrl"
                StringUtil.pullUrlFromString(newUrl)
            } else {
                val newUrl = "http://$url"
                StringUtil.pullUrlFromString(newUrl)
            }
        } else {
            StringUtil.pullUrlFromString(url)
        }
    }
}