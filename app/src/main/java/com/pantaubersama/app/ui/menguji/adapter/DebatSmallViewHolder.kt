package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.ui.debat.detail.DetailDebatActivity
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*

class DebatSmallViewHolder(view: View, fm: FragmentManager) : DebatViewHolder(view, fm) {

    override val textDebatType: TextView = text_debat_type
    override val imageAvatar1: ImageView = image_avatar_1
    override val imageAvatar2: ImageView = image_avatar_2
    override val textName1: TextView = text_name_1
    override val textName2: TextView = text_name_2
    override val textTopic: TextView = text_topic
    override val textStatement: TextView = text_statement
    override val textOpponentCount: TextView = text_opponent_count
    override val buttonMoreOption: View = button_more

    override fun bind(item: DebatItem) {
        super.bind(item)

        val bgImage = when (item) {
            is DebatItem.LiveNow -> R.drawable.bg_debat_live
            is DebatItem.ComingSoon -> R.drawable.bg_coming_soon
            is DebatItem.Done -> R.drawable.bg_done
            is DebatItem.Challenge -> R.drawable.bg_challenge
        }
        val vsBgImage = if (item is DebatItem.Challenge && item.isDirect)
            R.drawable.bg_versus_fill_yellow else R.drawable.bg_versus_outline_gray
        val vsTextColor = containerView.context.color(
            if (item is DebatItem.Challenge && item.isDirect) R.color.white else R.color.yellow)

        bg_carousel_item.setImageResource(bgImage)
        text_versus.setBackgroundResource(vsBgImage)
        text_versus.setTextColor(vsTextColor)
        icon_debat_type.visibleIf(item is DebatItem.LiveNow)
        text_status.visibleIf(item !is DebatItem.Done)
        text_favorite_count.visibleIf(item is DebatItem.Done)
        layout_clap.visibleIf(item is DebatItem.Done)

        val isNotDeniedNorExpired = item !is DebatItem.Challenge || (!item.isDenied && !item.isExpired)
        text_versus.visibleIf(isNotDeniedNorExpired)
        bg_bottom.setBackgroundResource(if (isNotDeniedNorExpired)
            R.drawable.bg_rounded_bottom else R.drawable.bg_rounded_bottom_fill_gray)
        text_status.setTextColor(itemView.context.color(if (isNotDeniedNorExpired)
            R.color.gray_4 else R.color.red_2))

        when (item) {
            is DebatItem.LiveNow -> text_status.text = "Live Selama 20 Menit"
            is DebatItem.ComingSoon -> {
                text_status.text = "${item.date}  •  ${item.startEndTime}"
            }
            is DebatItem.Done -> {
                text_clap_1.text = item.clap1.toString()
                text_clap_2.text = item.clap2.toString()
                text_favorite_count.text = item.favoriteCount.toString()
            }
            is DebatItem.Challenge -> {
                text_status.text = when {
                    item.isDenied -> "Lawan Menolak Tantangan"
                    item.isExpired -> "Tantangan Melebihi Batas Waktu"
                    item.opponentCandidates > 0 -> "Menunggu Konfirmasi"
                    else -> "Menunggu Lawan Debat"
                }
            }
        }

        itemView.setOnClickListener { itemView.context.let { it.startActivity(DetailDebatActivity.setIntent(it, item)) } }
    }
}