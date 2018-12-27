package com.pantaubersama.app.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OffsetItemDecoration(
    var left: Int,
    var top: Int = left,
    var right: Int = left,
    var bottom: Int = left,
    var orientation: Int = RecyclerView.VERTICAL
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val isFirstItem = parent.getChildAdapterPosition(view) == 0

        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(left, if (isFirstItem) top else 0, right, bottom)
        } else {
            outRect.set(if (isFirstItem) left else 0, top, right, bottom)
        }
    }
}
