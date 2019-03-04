package com.pantaubersama.app.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pantaubersama.app.BuildConfig

fun Context.dip(value: Int): Int =
    (value * resources.displayMetrics.density).toInt()

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable? =
    AppCompatResources.getDrawable(this, drawableRes)

fun Context.color(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)

fun Fragment.dip(value: Int): Int = requireContext().dip(value)

fun Fragment.drawable(@DrawableRes drawableRes: Int): Drawable? =
    requireContext().drawable(drawableRes)

fun Fragment.color(@ColorRes colorRes: Int): Int =
    requireContext().color(colorRes)

inline fun Activity.checkPermission(permission: String, requestCode: Int, onGranted: () -> Unit) {
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
        onGranted()
    else
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}

fun Context.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + BuildConfig.APPLICATION_ID)))
}

fun Activity.inflate(@LayoutRes layoutRes: Int, root: ViewGroup? = null): View {
    return layoutInflater.inflate(layoutRes, root)
}