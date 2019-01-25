package com.pantaubersama.app.ui.search.history

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_search_keyword.*

class SearchHistoryAdapter(private var listener: AdapterListener) : RecyclerView.Adapter<SearchHistoryAdapter.KeywordViewHolder>() {

    var data: MutableList<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryAdapter.KeywordViewHolder {
        return KeywordViewHolder(parent.inflate(R.layout.item_search_keyword))
    }

    override fun onBindViewHolder(holder: SearchHistoryAdapter.KeywordViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class KeywordViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: String) {
            tv_search_keyword.text = item
            tv_search_keyword.setOnClickListener { listener.onClick(item) }
            tv_search_keyword.setOnLongClickListener {
                listener.onLongClick(item, adapterPosition)
                true
            }
        }
    }

    interface AdapterListener {
        fun onClick(keyword: String)
        fun onLongClick(keyword: String, position: Int)
    }

    fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}