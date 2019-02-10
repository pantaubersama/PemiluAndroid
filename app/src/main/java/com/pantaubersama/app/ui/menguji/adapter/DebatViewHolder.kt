package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.data.model.debat.DebatItem
import kotlinx.android.extensions.LayoutContainer

abstract class DebatViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract val textDebatType: TextView
    abstract val textName1: TextView
    abstract val textName2: TextView
    abstract val textTag: TextView

    open fun bind(item: DebatItem) {
        textDebatType.text = item.type
        textName1.text = item.debatDetail.name1
        textName2.text = item.debatDetail.name2
        textTag.text = item.debatDetail.tag
    }
}