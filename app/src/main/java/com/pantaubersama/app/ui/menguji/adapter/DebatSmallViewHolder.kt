package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*

class DebatSmallViewHolder(view: View) : DebatViewHolder(view) {

    override val textDebatType: TextView = text_debat_type
    override val imageAvatar1: ImageView = image_avatar_1
    override val imageAvatar2: ImageView = image_avatar_2
    override val textName1: TextView = text_name_1
    override val textName2: TextView = text_name_2
    override val textTag: TextView = text_tag
    override val textOpponentCount: TextView = text_opponent_count

    override fun bind(item: DebatItem) {
        super.bind(item)

        val bgImage = when (item) {
            is DebatItem.LiveNow -> R.drawable.bg_debat_live
            is DebatItem.ComingSoon -> R.drawable.bg_coming_soon
            is DebatItem.Done -> R.drawable.bg_done
            is DebatItem.Open -> R.drawable.bg_challenge
        }

        bg_carousel_item.setImageResource(bgImage)
        icon_debat_type.visibleIf(item is DebatItem.LiveNow)
        text_status.visibleIf(item !is DebatItem.Done)
        text_favorite_count.visibleIf(item is DebatItem.Done)
        layout_clap.visibleIf(item is DebatItem.Done)

        when (item) {
            is DebatItem.LiveNow -> text_status.text = "Live selama 20 menit"
            is DebatItem.ComingSoon -> text_status.text = "${item.date}  •  ${item.startEndTime}"
            is DebatItem.Done -> {
                text_clap_1.text = item.clap1.toString()
                text_clap_2.text = item.clap2.toString()
                text_favorite_count.text = item.favoriteCount.toString()
            }
            is DebatItem.Open -> {
                text_status.text = if (item.pendingOpponent > 0)
                    "Waiting for confirmation" else "Waiting for opponent"
            }
        }
    }
}