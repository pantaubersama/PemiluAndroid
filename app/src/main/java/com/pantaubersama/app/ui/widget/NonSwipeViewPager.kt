package com.pantaubersama.app.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeViewPager : ViewPager {
    var isSwipeable = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {

        return if (isSwipeable) {
            super.onInterceptTouchEvent(arg0)
        } else false

        // Never allow swiping to switch between pages
    }
}