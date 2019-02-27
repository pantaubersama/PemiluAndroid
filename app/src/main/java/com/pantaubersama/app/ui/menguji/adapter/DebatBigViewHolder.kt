package com.pantaubersama.app.ui.menguji.adapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.ui.debat.DebatActivity
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_big.*

class DebatBigViewHolder(view: View, fm: FragmentManager) : DebatViewHolder(view, fm) {

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
            is DebatItem.Challenge -> R.drawable.bg_debat_open
            else -> 0
        }
        val colorRes = if (item is DebatItem.LiveNow) R.color.black_1 else R.color.gray_4

        bg_carousel_item.setImageResource(bgImage)
        icon_debat_type.visibleIf(item is DebatItem.LiveNow)
        textName2.setTextColor(itemView.context.color(colorRes))

        when (item) {
            is DebatItem.Challenge -> {
                textName2.text = if (item.opponentCandidates > 0)
                    "Waiting for confirmation" else "Waiting for opponent"
            }
            is DebatItem.LiveNow -> {
                itemView.setOnClickListener { itemView.context?.let { it.startActivity(Intent(it, DebatActivity::class.java)) } }
            }
        }
    }
}