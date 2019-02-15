package com.pantaubersama.app.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OffsetItemDecoration(
    var left: Int,
    var top: Int = left,
    var right: Int = left,
    var bottom: Int = top,
    var orientation: Int = RecyclerView.VERTICAL,
    var ignoreFirstAndLast: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val isFirstItem = position == 0
        val isLastItem = position == parent.adapter?.itemCount?.let { it - 1 }

        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(left, if (ignoreFirstAndLast || !isFirstItem) 0 else top,
                right, if (ignoreFirstAndLast && isLastItem) 0 else bottom)
        } else {
            outRect.set(if (ignoreFirstAndLast || !isFirstItem) 0 else left, top,
                if (ignoreFirstAndLast && isLastItem) 0 else right, bottom)
        }
    }
}
