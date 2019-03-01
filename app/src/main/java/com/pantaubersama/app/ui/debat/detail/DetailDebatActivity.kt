package com.pantaubersama.app.ui.debat.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.*
import kotlinx.android.synthetic.main.activity_detail_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_centered_title.*

class DetailDebatActivity : BaseActivity<DetailDebatPresenter>() {
    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_detail_debat
    override lateinit var presenter: DetailDebatPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    lateinit var challenge: Challenge

    var isLiked = false

    companion object {
        fun setIntent(context: Context, challenge: Challenge): Intent {
            val intent = Intent(context, DetailDebatActivity::class.java)
            intent.putExtra(EXTRA_CHALLENGE_ITEM, challenge)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(EXTRA_CHALLENGE_ITEM)?.let { challenge = it as Challenge }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupHeader()
        setupContent()
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)

        lottie_love.progress = if (isLiked) 1.0f else 0.0f

        btn_like.setOnClickListener {
            if (!animator.isRunning) {
                isLiked = !isLiked
                if (isLiked) {
                    tv_like_count.text = (tv_like_count.text.toString().toInt() + 1).toString()
                    animator.addUpdateListener { animation ->
                        lottie_love.progress = animation.animatedValue as Float
                    }
                    animator.start()
                } else {
                    tv_like_count.text = (tv_like_count.text.toString().toInt() - 1).toString()
                    lottie_love.progress = 0.0f
                }
            }
        }

        btn_back.setOnClickListener { onBackPressed() }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupHeader() {
        tv_toolbar_title.text = challenge.status
        cl_clap_count.visibleIf(challenge.status == Status.DONE)
        tv_denied_debat.visibleIf(challenge.status in arrayOf(Status.DENIED, Status.EXPIRED))
        tv_denied_debat.apply { if (isVisible()) text = challenge.status }

        tv_name_challenger.text = challenge.challenger.fullName
        iv_avatar_challenger.loadUrl(challenge.challenger.avatar.medium?.url, R.color.gray_3)

        val showAvatarOpponent = challenge.progress != Progress.WAITING_OPPONENT || challenge.status !in arrayOf(Status.DENIED, Status.EXPIRED)
        iv_avatar_opponent.visibleIf(showAvatarOpponent )
        ll_text_opponent.visibleIf(challenge.progress in arrayOf(Progress.WAITING_OPPONENT, Status.DENIED, Status.EXPIRED))

        val avatarOpponent = challenge.opponent?.avatar?.thumbnailSquare?.url
            ?: challenge.opponentCandidates.firstOrNull()?.avatar?.thumbnailSquare?.url
        iv_avatar_opponent.apply { if (isVisible()) {
            loadUrl(avatarOpponent, R.color.gray_3)
            val count = when {
                challenge.opponentCandidates.size == 1 -> "?"
                challenge.opponentCandidates.size > 1 -> challenge.opponentCandidates.toString()
                else -> null
            }
            tv_opponent_count.text = count
            tv_opponent_count.visibleIf(count != null)
        } }

        btn_more.setOnClickListener {
            val showDeleteButton = challenge.progress == ChallengeConstants.Progress.WAITING_OPPONENT
            optionDialog.setViewVisibility(R.id.delete_action, showDeleteButton)
            optionDialog.listener = View.OnClickListener {
                when (it.id) {
                    R.id.copy_link_action -> ToastUtil.show(it.context, "Salin Tautan")
                    R.id.share_action -> ToastUtil.show(it.context, "Bagikan")
                    R.id.delete_action -> ToastUtil.show(it.context, "Hapus")
                }
                optionDialog.dismiss()
            }
            optionDialog.show(supportFragmentManager, "dialog")
        }

        val bgToolbarColor = color(when (challenge.status) {
            Status.COMING_SOON -> R.color.blue_2
            Status.DONE -> R.color.purple_5
            in arrayOf(Status.OPEN_CHALLENGE, Status.DIRECT_CHALLENGE, Status.EXPIRED, Status.DENIED) -> R.color.orange_3
            else -> throw ErrorException("Status debat tidak sesuai")
        })

        val bgHeader = when (challenge.status) {
            Status.COMING_SOON -> R.drawable.banner_coming_soon_fullheight
            Status.DONE -> R.drawable.banner_done_post
            in arrayOf(Status.OPEN_CHALLENGE, Status.DIRECT_CHALLENGE, Status.EXPIRED, Status.DENIED) -> R.drawable.banner_challenge_big
            else -> throw ErrorException("Status debat tidak sesuai")
        }

        toolbar_detail_debat.setBackgroundColor(bgToolbarColor)
        cl_header.setBackgroundResource(bgHeader)
    }

    private fun setupContent() {
//        lateinit var contentDetail: View
//        val contentDetail = layoutInflater.inflate(when (challenge.status) {
//            Status.COMING_SOON -> R.layout.layout_content_detail_debat_coming_soon
//            Status.DONE -> R.layout.layout_content_detail_debat_done
//            Status.OPEN_CHALLENGE -> {
//                when ()
//            }
//            in arrayOf(Status.OPEN_CHALLENGE, Status.DIRECT_CHALLENGE, Status.EXPIRED, Status.DENIED) -> R.layout.layout_content_detail_debat_coming_soon
//            else -> throw ErrorException("Status debat tidak sesuai")
//        }, null)

        when (challenge.status) {
            Status.COMING_SOON -> onComingSoon()
            Status.DONE -> onDone()
            Status.OPEN_CHALLENGE -> onOpenChallenge()
        }
    }

    private fun onComingSoon() {
        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_coming_soon, null))
        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_detail_debat, null))
    }

    private fun onDone() {
        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_done, null))
        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_detail_debat, null))
    }

    private fun onOpenChallenge() {

    }
}