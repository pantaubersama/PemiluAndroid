package com.pantaubersama.app.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

class GlideImageGetter(private val context: Context?, private val textView: TextView?) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val urlDrawable = UrlDrawable()
        GlideApp.with(context!!)
                .asBitmap()
                .load(source)
                .dontAnimate()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val drawable: BitmapDrawable? = BitmapDrawable(context.resources, resource)
                        textView?.post {
                            val w = if (drawable != null) {
                                (textView.parent as View).width
                            } else {
                                textView.width
                            }
                            val hh = drawable?.intrinsicHeight
                            val ww = drawable?.intrinsicWidth
                            val newHeight = hh!! * w / ww!!
                            val rect = Rect(0, 0, w, newHeight)
                            drawable.bounds = rect
                            urlDrawable.bounds = rect
                            urlDrawable.drawable = drawable
                            textView.text = textView.text
                            textView.invalidate()
                        }
                    }
                })
        return urlDrawable
    }

    internal inner class UrlDrawable : BitmapDrawable() {
        var drawable: Drawable? = null
        override fun draw(canvas: Canvas) {
            if (drawable != null) {
                drawable!!.draw(canvas)
            }
        }
    }
}