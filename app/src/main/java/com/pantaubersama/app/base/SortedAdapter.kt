package com.pantaubersama.app.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import timber.log.Timber

/**
 * @author edityomurti on 12/02/2019 15:55
 */
abstract class SortedAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var data: SortedList<T>

    protected abstract val itemClass: Class<T>

    init {
        data = SortedList(itemClass, object : SortedList.Callback<T>() {
            override fun areItemsTheSame(oldItem: T?, newItem: T?): Boolean {
                return oldItem?.equals(newItem) == true
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
                notifyItemChanged(toPosition)
            }

            override fun onChanged(position: Int, count: Int) {}

            override fun onInserted(position: Int, count: Int) {}

            override fun onRemoved(position: Int, count: Int) {}

            override fun compare(item1: T, item2: T): Int {
                return this@SortedAdapter.compare(item1, item2)
            }

            override fun areContentsTheSame(oldItem: T?, newItem: T?): Boolean {
                return oldItem?.equals(newItem) == true
            }
        })
    }

    protected abstract fun compare(item1: T, item2: T): Int

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(getItemResourceLayout(viewType), parent, false)
    }

    protected abstract fun getItemResourceLayout(viewType: Int): Int

    override fun getItemCount(): Int {
        return data.size()
    }

    fun getDatas(): SortedList<T>? {
        return data
    }

    fun getData(position: Int): T? {
        return getDatas()?.get(position)
    }

    fun setDatas(items: List<T>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addData(items: List<T>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        val i = data.add(item)
        Timber.d("addItem i : $i")
        notifyItemInserted(i)
    }

    fun updateItem(item: T, position: Int) {
        data.updateItemAt(position, item)
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int) {
        if (position < itemCount) {
            data.removeItemAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(item: T) {
        data.remove(item)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }
}