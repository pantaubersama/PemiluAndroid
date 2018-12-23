package com.pantaubersama.app.utils

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class LineDividerItemDecoration(
        private val color: Int,
        private val height: Int,
        private val paddingLeft: Int = 0,
        private val paddingRight: Int = paddingLeft,
        private val skipFirst: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val paint: Paint = Paint()

    init {
        paint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + paddingLeft
        val right = parent.width - parent.paddingRight - paddingRight

        val childCount = parent.childCount
        for (i in (if (skipFirst) 1 else 0) until childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + height

            paint.color = color
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}