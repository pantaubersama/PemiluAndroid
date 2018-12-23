package com.pantaubersama.app.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable?
        = AppCompatResources.getDrawable(this, drawableRes)

fun Context.color(@ColorRes colorRes: Int): Int
        = ContextCompat.getColor(this, colorRes)

fun Fragment.dip(value: Int): Int = requireContext().dip(value)

fun Fragment.drawable(@DrawableRes drawableRes: Int): Drawable? = requireContext().drawable(drawableRes)

fun Fragment.color(@ColorRes colorRes: Int): Int = requireContext().color(colorRes)

