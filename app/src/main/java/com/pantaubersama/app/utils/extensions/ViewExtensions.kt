package com.pantaubersama.app.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.GlideApp

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visibleIf(condition: Boolean, invisible: Boolean = false) {
    val hide = if (invisible) View.INVISIBLE else View.GONE
    this.visibility = if (condition) View.VISIBLE else hide
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
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

fun ImageView.setGrayScale(isGray: Boolean) {
    colorFilter = if (isGray)
        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    else
        null
}

fun LinearLayout.enable(isEnable: Boolean) {
    if (isEnable) {
        this.isEnabled = true
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            child.isEnabled = true
        }
    } else {
        this.isEnabled = false
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            child.isEnabled = false
        }
    }
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun Int.toDP(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
}

fun LottieAnimationView.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
        this.playAnimation()
        this.repeatCount = LottieDrawable.INFINITE
    } else {
        this.visibility = View.GONE
        this.cancelAnimation()
    }
}

/* used only for layout_fail_state.xml */
fun LinearLayout.failStateVisible(isVisible: Boolean) {
    val lottieView = this.findViewById<LottieAnimationView>(R.id.lottie_fail_state)
    if (isVisible) {
        this.visibility = View.VISIBLE
        lottieView.playAnimation()
        lottieView.repeatCount = LottieDrawable.INFINITE
    } else {
        this.visibility = View.GONE
        lottieView.cancelAnimation()
    }
}

/* used only for layout_empty_state.xml */
fun LinearLayout.emptyStateVisible(isVisible: Boolean) {
    val lottieView = this.findViewById<LottieAnimationView>(R.id.lottie_empty_state)
    if (isVisible) {
        this.visibility = View.VISIBLE
        lottieView.playAnimation()
        lottieView.repeatCount = LottieDrawable.INFINITE
    } else {
        lottieView.cancelAnimation()
        this.visibility = View.GONE
    }
}