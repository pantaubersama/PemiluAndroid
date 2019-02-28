package com.pantaubersama.app.ui.menguji.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.DebatHeader
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_DEBAT_ITEM
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_HEADER
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_LOADING
import com.pantaubersama.app.utils.extensions.inflate

class DebatListAdapter(private val fm: FragmentManager) : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.item_debat_header) as TextView)
            TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            TYPE_DEBAT_ITEM -> DebatSmallViewHolder(parent.inflate(R.layout.item_debat_small), fm)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) holder.bind(data[position] as DebatHeader)
        if (holder is DebatSmallViewHolder) holder.bind(data[position] as Challenge)
    }

    class HeaderViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {

        fun bind(header: DebatHeader) {
            textView.text = header.text
        }
    }
}