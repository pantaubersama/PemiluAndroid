package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer

abstract class DebatViewHolder(
    override val containerView: View,
    private val fragmentManager: FragmentManager
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract val textStatus: TextView
    abstract val imageAvatar1: ImageView
    abstract val imageAvatar2: ImageView
    abstract val textName1: TextView
    abstract val textName2: TextView
    abstract val textTopic: TextView
    abstract val textStatement: TextView
    abstract val textOpponentCount: TextView
    abstract val buttonMoreOption: View

    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    open fun bind(challenge: Challenge) {
        val opponent = challenge.opponent ?: challenge.opponentCandidates.firstOrNull()

        textStatus.text = challenge.status
        textName1.text = challenge.challenger.fullName ?: challenge.challenger.username
        textName2.text = opponent?.fullName ?: opponent?.username
        textTopic.text = challenge.topicList.firstOrNull()
        textStatement.text = challenge.statement

        val showAvatar2 = opponent != null &&
            challenge.status !in arrayOf(Status.DENIED, Status.EXPIRED)
        imageAvatar1.loadUrl(challenge.challenger.avatar?.thumbnailSquare?.url,
            R.drawable.ic_avatar_placeholder)
        imageAvatar2.loadUrl(opponent?.avatar?.thumbnailSquare?.url,
            R.drawable.ic_avatar_placeholder)
        imageAvatar2.visibleIf(showAvatar2, true)

        val statusColor = containerView.context.color(
            if (challenge.status == Status.DIRECT_CHALLENGE) R.color.black_2
            else R.color.white)
        textStatus.setTextColor(statusColor)

        setupOpponentCandidateCount(challenge)

        buttonMoreOption.setOnClickListener {
            showOptionDialog(challenge)
        }
    }

    private fun showOptionDialog(challenge: Challenge) {
//        val showDeleteButton = challenge.progress == Progress.WAITING_OPPONENT //disable dulu @edityo 30/03/19
        val showDeleteButton = false
        optionDialog.setViewVisibility(R.id.delete_action, showDeleteButton)
        optionDialog.listener = View.OnClickListener {
            when (it.id) {
                R.id.copy_link_action -> CopyUtil.copyChallenge(it.context, challenge)
                R.id.share_action -> ShareUtil.shareItem(it.context, challenge)
//                R.id.delete_action -> ToastUtil.show(it.context, "Hapus")
            }
            optionDialog.dismiss()
        }

        optionDialog.show(fragmentManager, "dialog")
    }

    private fun setupOpponentCandidateCount(challenge: Challenge) {
        val count = when {
            challenge.opponent != null -> "?"
            challenge.opponentCandidates.size == 1 -> "?"
            challenge.opponentCandidates.size > 1 -> challenge.opponentCandidates.size.toString()
            else -> null
        }?.takeIf {
            challenge.progress in arrayOf(Progress.WAITING_OPPONENT, Progress.WAITING_CONFIRMATION)
        }

        textOpponentCount.text = count
        textOpponentCount.visibleIf(count != null)
    }
}