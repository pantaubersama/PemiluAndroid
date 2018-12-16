package com.pantaubersama.app.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener

/**
 * @author edityomurti on 16/12/2018 01:42
 */
abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener{

    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    constructor(itemView: View, itemClickListener: OnItemClickListener,
                itemLongClickListener: OnItemLongClickListener) : this(itemView) {
        this.itemClickListener = itemClickListener
        this.itemLongClickListener = itemLongClickListener
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    abstract fun bind(item: Any?)

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