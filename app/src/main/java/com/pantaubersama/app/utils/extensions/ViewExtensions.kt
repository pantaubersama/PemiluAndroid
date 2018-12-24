package com.pantaubersama.app.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visibleIf(condition: Boolean, invisible: Boolean = false) {
    val hide = if (invisible) View.INVISIBLE else View.GONE
    this.visibility = if (condition) View.VISIBLE else hide
}

fun Button.setBackgroundTint(color: String) {
    backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
}