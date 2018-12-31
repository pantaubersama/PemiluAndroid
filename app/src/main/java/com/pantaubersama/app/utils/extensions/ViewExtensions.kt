package com.pantaubersama.app.utils.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.pantaubersama.app.utils.GlideApp

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visibleIf(condition: Boolean, invisible: Boolean = false) {
    val hide = if (invisible) View.INVISIBLE else View.GONE
    this.visibility = if (condition) View.VISIBLE else hide
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Button.setBackgroundTint(@ColorRes color: Int) {
    backgroundTintList = ColorStateList.valueOf(context.color(color))
}

fun ImageView.loadUrl(
        url: String?,
        placeholderResId: Int? = null,
        errorResId: Int? = null,
        requestListener: RequestListener<Drawable>? = null
) {
    GlideApp.with(this)
            .load(url)
            .optionalFitCenter()
            .apply {
                placeholderResId?.let { placeholder(it) }
                errorResId?.let { error(it) }
                requestListener?.let { listener(it) }
            }
            .into(this)
}