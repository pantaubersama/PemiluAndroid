package com.pantaubersama.app.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.pantaubersama.app.data.model.ItemModel

/**
 * @author edityomurti on 12/02/2019 15:55
 */
abstract class SortedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var data: SortedList<ItemModel>?

    protected abstract val itemClass: Class<ItemModel>

    init {
        data = SortedList(itemClass, object : SortedList.Callback<ItemModel>() {
            override fun areItemsTheSame(oldItem: ItemModel?, newItem: ItemModel?): Boolean {
                return oldItem == newItem
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
                notifyItemChanged(toPosition)
            }

            override fun onChanged(position: Int, count: Int) {}

            override fun onInserted(position: Int, count: Int) {}

            override fun onRemoved(position: Int, count: Int) {}

            override fun compare(item1: ItemModel, item2: ItemModel): Int {
                return this@SortedAdapter.compare(item1, item2)
            }

            override fun areContentsTheSame(oldItem: ItemModel?, newItem: ItemModel?): Boolean {
                return oldItem == newItem
            }

        })
    }

    protected abstract fun compare(item1: ItemModel, item2: ItemModel): Int

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(getItemResourceLayout(viewType), parent, false)
    }

    protected abstract fun getItemResourceLayout(viewType: Int): Int

    override fun getItemCount(): Int {
        return data?.size() ?: 0
    }

    fun setDatas(items: List<ItemModel>) {
        data?.clear()
        data?.addAll(items)
        notifyDataSetChanged()
    }

    fun addData(items: List<ItemModel>) {
        data?.addAll(items)
        notifyItemRangeInserted(itemCount - 1, items.size)
    }

    fun addItem(item: ItemModel) {
        data?.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun deleteItem(position: Int) {
        if (position < itemCount) {
            data?.removeItemAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(item: ItemModel) {
        data?.remove(item)
    }

    fun clear() {
        data?.clear()
        notifyDataSetChanged()
    }
}