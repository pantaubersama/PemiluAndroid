package com.pantaubersama.app.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import timber.log.Timber

/**
 * @author edityomurti on 16/12/2018 02:12
 */
@Deprecated("Changed to BaseRecyclerAdapter")
abstract class BaseAdapterDEL<T, V : BaseViewHolder<T>>(context: Context) : RecyclerView.Adapter<V>() {
    protected var data = ArrayList<T>()
    protected var context: Context? = null

    private var lastVisibleItem: Int? = null
    private var totalItemCount: Int? = null
    private var totalItemBeforeLoadMore: Int? = null
    private var visibleTreshold = 10
    private var page = 1
    private var isLoadingMore = false
    private var isDataEnd = false
    private var loadMoreListener: OnLoadMoreListener? = null

    fun addSupportLoadMore(recyclerView: RecyclerView, loadMoreListener: OnLoadMoreListener) {
        var layoutManager = recyclerView.layoutManager
        if (layoutManager == null) {
            Timber.e("set layout manager first")
        }

        if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager
            totalItemBeforeLoadMore = linearLayoutManager.itemCount
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    totalItemCount = linearLayoutManager.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!isDataEnd && !isLoadingMore && totalItemCount!! <= (lastVisibleItem!! + visibleTreshold)) {
                        page++
                        loadMoreListener.loadMore(page)
                        isLoadingMore = true
                    }

//                    if (totalItemBeforeLoadMore != totalItemCount) {
//                        isLoadingMore = false
//                        totalItemBeforeLoadMore = totalItemCount
//                    }
                }
            })
        } else {
            Timber.e("only support linearlayout")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun get(position: Int): T {
        return data.get(position)
    }

    fun getData(): MutableList<T> {
        return data
    }

    fun replaceData(items: MutableList<T>) {
        data.clear()
        add(items)
    }

    fun add(item: T) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun add(item: T, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    open fun add(items: MutableList<T>) {
        for (item in items) {
            data.add(item)
        }
        notifyDataSetChanged()
    }

    fun add(items: Array<T>) {
        add(items.toMutableList())
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: T) {
        val i = data.indexOf(item)
        if (i >= 0) {
            data.set(i, item)
            notifyItemChanged(i)
        } else {
            add(item)
        }
    }

    fun addOrUpdate(items: MutableList<T>, reverse: Boolean) {
        if (reverse) {
            for (item in items) {
                val i = data.indexOf(item)
                if (i >= 0) {
                    data[i] = item
                } else {
                    data.add(item)
                }
            }

            data.reverse()
            notifyDataSetChanged()
        } else {
            addOrUpdate(items)
        }
    }

    fun addOnTopOrUpdate(item: T) {
        val i = data.indexOf(item)
        if (i >= 0) {
            data.set(i, item)
            notifyDataSetChanged()
        } else {
            add(item, 0)
        }
    }

    fun addOnTopOrUpdate(items: MutableList<T>) {
        for (item in items) {
            addOnTopOrUpdate(item)
        }
    }

    fun addOrUpdate(items: MutableList<T>) {
        for (item in items) {
            addOrUpdate(item)
        }

        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= 0 && position < data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun remove(item: T) {
        data.remove(item)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun getPosition(item: T): Int {
        return data.indexOf(item)
    }

    fun setLoading(item: T) {
        isLoadingMore = true
        add(item)
    }

    fun setLoaded() {
        if (isLoadingMore) {
            isLoadingMore = false
            remove(data.size - 1)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val view = LayoutInflater.from(parent.context).inflate(setItemView(viewType)!!, parent, false)
        return initViewHolder(view, viewType)!!
    }

    protected abstract fun initViewHolder(view: View, viewType: Int): V?

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.bind(get(position))
    }

    @LayoutRes
    protected abstract fun setItemView(viewType: Int): Int?

    interface OnLoadMoreListener {
        fun loadMore(page: Int)
    }
}