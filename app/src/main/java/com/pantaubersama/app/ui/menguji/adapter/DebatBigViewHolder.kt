package com.pantaubersama.app.ui.menguji.adapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.ui.debat.DebatActivity
import com.pantaubersama.app.ui.debat.detail.DetailDebatActivity
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_big.*

class DebatBigViewHolder(view: View, fm: FragmentManager) : DebatViewHolder(view, fm) {

    override val textStatus: TextView = text_debat_type
    override val imageAvatar1: ImageView = image_avatar_1
    override val imageAvatar2: ImageView = image_avatar_2
    override val textName1: TextView = text_name_1
    override val textName2: TextView = text_name_2
    override val textTopic: TextView = text_topic
    override val textStatement: TextView = text_statement
    override val textOpponentCount: TextView = text_opponent_count
    override val buttonMoreOption: View = button_more

    override fun bind(challenge: Challenge) {
        super.bind(challenge)

        val bgImage = when (challenge.status) {
            Status.LIVE_NOW -> R.drawable.bg_debat_live
            Status.OPEN_CHALLENGE -> R.drawable.bg_debat_open
            else -> 0
        }
        val colorRes = if (challenge.status == Status.LIVE_NOW) R.color.black_1 else R.color.gray_4

        bg_carousel_item.setImageResource(bgImage)
        icon_debat_type.visibleIf(challenge.status == Status.LIVE_NOW)
        textName2.setTextColor(itemView.context.color(colorRes))

        if (challenge.status == Status.OPEN_CHALLENGE) {
            textName2.text = if (challenge.progress == Progress.WAITING_CONFIRMATION)
                "Menunggu Konfirmasi" else "Menunggu Lawan Debat"
        }

        itemView.setOnClickListener {
            if (challenge.status == Status.LIVE_NOW) {
                it.context.startActivity(DebatActivity.setIntent(it.context, challenge))
            } else {
              it.context.startActivity(DetailDebatActivity.setIntent(it.context, challenge))
            }
        }
    }
}