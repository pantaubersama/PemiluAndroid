package com.pantaubersama.app.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.pantaubersama.app.R

class BottomCloseButton @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {
//    constructor(context: Context) : super(context)
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    init {
        setPadding(12f.toDp(), 12f.toDp(), 12f.toDp(), 12f.toDp())
        val backImage = ImageView(context)
        backImage.setImageResource(R.drawable.ic_close)
        val backImageParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        backImageParams.gravity = Gravity.CENTER
        backImage.layoutParams = backImageParams
        var typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true)
        backImage.setBackgroundResource(typedValue.resourceId)
        setBackgroundResource(R.drawable.close_button_background)
        addView(backImage)
        isClickable = true
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.BottomCloseButton, 0, 0)
            val iconDrawable = typedArray.getResourceId(R.styleable.BottomCloseButton_icon, R.drawable.ic_close)
            backImage.setImageResource(iconDrawable)
            typedArray.recycle()
        }
    }

    private fun Float.toDp(): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, r.displayMetrics))
    }
}
