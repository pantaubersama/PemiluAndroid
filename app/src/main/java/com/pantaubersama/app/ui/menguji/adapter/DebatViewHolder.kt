package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer

abstract class DebatViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract val textDebatType: TextView
    abstract val imageAvatar1: ImageView
    abstract val imageAvatar2: ImageView
    abstract val textName1: TextView
    abstract val textName2: TextView
    abstract val textTag: TextView
    abstract val textOpponentCount: TextView

    open fun bind(item: DebatItem) {
        textDebatType.text = item.type
        textName1.text = item.debatDetail.name1
        textName2.text = item.debatDetail.name2
        textTag.text = item.debatDetail.tag

        imageAvatar2.visibleIf(item !is DebatItem.Open || item.pendingOpponent > 0, true)

        setupOpponentCount(item)
    }

    private fun setupOpponentCount(item: DebatItem) {
        val count = if (item is DebatItem.Open) when {
            item.pendingOpponent == 1 -> "?"
            item.pendingOpponent > 1 -> item.pendingOpponent.toString()
            else -> null
        } else null

        textOpponentCount.text = count
        textOpponentCount.visibleIf(item is DebatItem.Open && item.pendingOpponent > 0)
    }
}