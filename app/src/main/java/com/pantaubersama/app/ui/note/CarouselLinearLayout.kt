package com.pantaubersama.app.ui.note

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pantaubersama.app.ui.note.presiden.CarouselPagerAdapter

class CarouselLinearLayout : LinearLayout {
    private var scale = CarouselPagerAdapter.BIG_SCALE

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    fun setScaleBoth(scale: Float) {
        this.scale = scale
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width
        val h = height
        canvas.scale(scale, scale, w / 2f, h / 2f)
    }
}