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
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*
import java.text.SimpleDateFormat
import java.util.Locale

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
                val showTime = dateFormatFrom.parse(challenge.showTimeAt)
                text_status.text = dateFormatTo.format(showTime)
            }
            Status.DONE -> {
                text_clap_1.text = "70"
                text_clap_2.text = "70"
                text_favorite_count.text = "50"
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
                it.context.startActivity(Intent(it.context, DebatActivity::class.java))
            } else {
//                it.startActivity(DetailDebatActivity.setIntent(it, challenge))
            }
        }
    }

    companion object {
        private val dateFormatFrom = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale("in", "ID"))
        private val dateFormatTo = SimpleDateFormat("dd MMMM yyyy  '•'  hh:mm", Locale("in", "ID"))
    }
}