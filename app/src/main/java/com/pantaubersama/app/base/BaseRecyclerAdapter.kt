package com.pantaubersama.app.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.LoadingModel
import timber.log.Timber

abstract class BaseRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var data: MutableList<ItemModel> = ArrayList()

    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var totalItemBeforeLoadMore: Int? = null

    private var isLoadingMore = false
    private var isDataEnd = false

    private var page = 1

    fun addSupportLoadMore(
        recyclerView: RecyclerView,
        loadMoreListener: OnLoadMoreListener,
        visibleTreshold: Int
    ) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            if (layoutManager is LinearLayoutManager) {
                totalItemBeforeLoadMore = layoutManager.itemCount
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        totalItemCount = layoutManager.itemCount
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        if (!isDataEnd && !isLoadingMore && totalItemCount!! <= (lastVisibleItem!! + visibleTreshold)) {
                            page++
                            loadMoreListener.loadMore(page)
                            isLoadingMore = true
                        }
                    }
                })
            } else {
                Timber.e("Only support LinearLayotManager")
            }
        } else {
            Timber.e("No LayoutManager found")
        }
    }

    interface OnLoadMoreListener {
        fun loadMore(page: Int)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun get(position: Int): ItemModel {
        return data[position]
    }

    fun getPosition(item: ItemModel): Int {
        return data.indexOf(item)
    }

    open fun setDatas(items: List<ItemModel>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addData(items: List<ItemModel>) {
        data.addAll(items)
        notifyItemRangeInserted(itemCount - 1, items.size)
    }

    fun addItem(item: ItemModel) {
        data.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun addItem(item: ItemModel, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    fun deleteItem(position: Int) {
        if (position < itemCount) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(item: ItemModel) {
        data.remove(item)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun setLoading() {
        isLoadingMore = true
        addItem(LoadingModel())
    }

    fun setLoaded() {
        if (isLoadingMore) {
            isLoadingMore = false
            deleteItem(itemCount - 1)
        }
    }

    fun setDataEnd(isDataEnd: Boolean) {
        if (!isDataEnd) {
            page = 1
        }
        this.isDataEnd = isDataEnd
    }

    fun isDataEnd(): Boolean {
        return isDataEnd
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }
}