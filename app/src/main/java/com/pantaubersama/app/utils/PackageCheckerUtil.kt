package com.pantaubersama.app.utils

import android.content.Context
import android.content.pm.PackageManager

class PackageCheckerUtil {
    companion object {
        fun isTwitterAppInstalled(context: Context): Boolean {
            val pkManager = context.packageManager
            var isInstalled = false
            try {
                val pkgInfo = pkManager?.getPackageInfo("com.twitter.android", 0)
                val getPkgInfo = pkgInfo.toString()
                if (getPkgInfo.contains("com.twitter.android")) {
                    isInstalled = true
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()

                isInstalled = false
            }
            return isInstalled
        }
    }
}