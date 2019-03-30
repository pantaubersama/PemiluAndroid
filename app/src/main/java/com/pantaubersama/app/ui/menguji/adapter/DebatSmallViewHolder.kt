package com.pantaubersama.app.ui.menguji.adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.ui.debat.DebatActivity
import com.pantaubersama.app.ui.debat.detail.DetailDebatActivity
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_DEBAT
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_CHALLENGE
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.parseDate
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*

class DebatSmallViewHolder(view: View, fm: FragmentManager) : DebatViewHolder(view, fm) {

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
            Status.COMING_SOON -> R.drawable.bg_coming_soon
            Status.DONE -> R.drawable.bg_done
            else -> R.drawable.bg_challenge
        }
        val vsBgImage = if (challenge.status == Status.DIRECT_CHALLENGE)
            R.drawable.bg_versus_fill_yellow else R.drawable.bg_versus_outline_gray
        val vsTextColor = containerView.context.color(
            if (challenge.status == Status.DIRECT_CHALLENGE) R.color.white else R.color.yellow)

        bg_carousel_item.setImageResource(bgImage)
        text_versus.setBackgroundResource(vsBgImage)
        text_versus.setTextColor(vsTextColor)
        icon_debat_type.visibleIf(challenge.status == Status.LIVE_NOW)
        text_status.visibleIf(challenge.status != Status.DONE)
        text_favorite_count.visibleIf(challenge.status == Status.DONE)
        layout_clap.visibleIf(challenge.status == Status.DONE)

        val isDeniedOrExpired = challenge.status in arrayOf(Status.DENIED, Status.EXPIRED)
        text_versus.visibleIf(!isDeniedOrExpired)
        bg_bottom.setBackgroundResource(if (!isDeniedOrExpired)
            R.drawable.bg_rounded_bottom else R.drawable.bg_rounded_bottom_fill_gray)
        text_status.setTextColor(itemView.context.color(if (!isDeniedOrExpired)
            R.color.gray_4 else R.color.red_2))

        when (challenge.status) {
            Status.LIVE_NOW -> text_status.text = "Live Selama 20 Menit"
            Status.COMING_SOON -> {
                text_status.text = challenge.showTimeAt.parseDate()
            }
            Status.DONE -> {
                text_clap_1.text = challenge.challenger.clapCount?.toString() ?: "0"
                text_clap_2.text = challenge.opponent?.clapCount?.toString() ?: "0"
                text_favorite_count.text = challenge.likeCount?.toString() ?: "0"
            }
            else -> {
                text_status.text = when {
                    challenge.status == Status.DENIED -> "Lawan Menolak Tantangan"
                    challenge.status == Status.EXPIRED -> "Tantangan Melebihi Batas Waktu"
                    challenge.progress == Progress.WAITING_CONFIRMATION -> "Menunggu Konfirmasi"
                    else -> "Menunggu Lawan Debat"
                }
            }
        }

        itemView.setOnClickListener {
            if (challenge.status == Status.LIVE_NOW) {
                it.context.startActivity(DebatActivity.setIntent(it.context, challenge))
            } else {
                (it.context as FragmentActivity)
                    .startActivityForResult(
                        DetailDebatActivity.setIntent(it.context, challenge, adapterPosition),
                        RC_OPEN_DETAIL_DEBAT)
            }
        }
    }
}