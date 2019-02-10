package com.pantaubersama.app.ui.menguji.viewadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.inflate

/**
 * This adapter is used for showing only a few [DebatItem]s on the RecyclerView. So it doesn't
 * support pagination.
 * */
class BriefDebatAdapter(
    private val displayBigView: Boolean
) : RecyclerView.Adapter<DebatViewHolder>() {

    var debatItems: List<DebatItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = debatItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebatViewHolder {
        return if (displayBigView)
            DebatBigViewHolder(parent.inflate(R.layout.item_debat_big))
        else
            DebatSmallViewHolder(parent.inflate(R.layout.item_debat_small))
    }

    override fun onBindViewHolder(holder: DebatViewHolder, position: Int) {
        holder.bind(debatItems[position])
    }
}