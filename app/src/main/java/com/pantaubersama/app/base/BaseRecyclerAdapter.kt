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
        visibleTreshold: Int,
        onLoadMore: (page: Int) -> Unit
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
                            onLoadMore(page)
                            isLoadingMore = true
                        }
                    }
                })
            } else {
                Timber.e("Only support LinearLayoutManager")
            }
        } else {
            Timber.e("No LayoutManager found")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun get(position: Int): ItemModel? {
        return if (position < data.size) data[position] else null
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

    open fun addItem(item: ItemModel) {
        data.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun addItem(item: ItemModel, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    fun changeItem(item: ItemModel, position: Int) {
        data.add(position, item)
        notifyItemChanged(position)
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
            if (itemCount > 0) {
                if (data[itemCount - 1] is LoadingModel) deleteItem(itemCount - 1)
            }
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

    fun getListData(): MutableList<ItemModel> {
        return data
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }
}