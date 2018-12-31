package com.pantaubersama.app.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by alimustofa on 05/02/18.
 */
abstract class OnScrollListener(val layoutManager: LinearLayoutManager?): RecyclerView.OnScrollListener(){
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = layoutManager?.itemCount
        val lastVisibleItem = layoutManager?.findLastVisibleItemPosition()
        val visibleThreshold = layoutManager?.childCount

        if (!isLoading() && !isDataEnd() && totalItemCount!! <= lastVisibleItem!! + visibleThreshold!!) {
            loadMoreItem()
        }
    }

    abstract fun loadMoreItem()

    abstract fun isDataEnd(): Boolean

    abstract fun isLoading(): Boolean
}