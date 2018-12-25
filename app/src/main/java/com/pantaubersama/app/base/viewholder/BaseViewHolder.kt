package com.pantaubersama.app.base.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener

/**
 * @author edityomurti on 16/12/2018 01:42
 */
abstract class BaseViewHolder<T>(itemView: View, private var itemClickListener: OnItemClickListener?, private var itemLongClickListener: OnItemLongClickListener?) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    abstract fun bind(item: T)

    override fun onClick(v: View?) {
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(v!!, adapterPosition)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (itemLongClickListener != null) {
            itemLongClickListener!!.onItemLongClick(v!!, adapterPosition)
            return true
        }
        return false
    }
}