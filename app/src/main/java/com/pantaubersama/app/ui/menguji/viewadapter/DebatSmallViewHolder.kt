package com.pantaubersama.app.ui.menguji.viewadapter

import android.view.View
import android.widget.TextView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*

class DebatSmallViewHolder(view: View) : DebatViewHolder(view) {

    override val textDebatType: TextView = text_debat_type
    override val textName1: TextView = text_name_1
    override val textName2: TextView = text_name_2
    override val textTag: TextView = text_tag

    override fun bind(item: DebatItem) {
        super.bind(item)

        val bgImage = when (item) {
            is DebatItem.ComingSoon -> R.drawable.bg_coming_soon
            is DebatItem.Done -> R.drawable.bg_done
            else -> 0
        }

        bg_carousel_item.setImageResource(bgImage)
        text_status.visibleIf(item is DebatItem.ComingSoon)
        layout_clap.visibleIf(item is DebatItem.Done)
        text_favorite_count.visibleIf(item is DebatItem.Done)

        when (item) {
            is DebatItem.ComingSoon -> text_status.text = "${item.date}  •  ${item.startEndTime}"
            is DebatItem.Done -> {
                text_clap_1.text = item.clap1.toString()
                text_clap_2.text = item.clap2.toString()
                text_favorite_count.text = item.favoriteCount.toString()
            }
        }
    }
}