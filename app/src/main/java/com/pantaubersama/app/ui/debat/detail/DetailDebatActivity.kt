package com.pantaubersama.app.ui.debat.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.makeramen.roundedimageview.RoundedImageView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Audience
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.DebatActivity
import com.pantaubersama.app.ui.debat.TerimaChallengeDialog
import com.pantaubersama.app.ui.debat.TolakChallengeDialog
import com.pantaubersama.app.ui.debat.adapter.OpponentCandidateAdapter
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ID
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import com.pantaubersama.app.utils.previewTweet
import com.pantaubersama.app.utils.previewUrl
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_detail_debat.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_centered_title.*
import javax.inject.Inject

class DetailDebatActivity : BaseActivity<DetailDebatPresenter>(), DetailDebatView {
    private var isMyChallenge = false

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_detail_debat

    @Inject
    override lateinit var presenter: DetailDebatPresenter

    private var challenge: Challenge? = null
    private var challengeId: String? = null

    private var isLiked = false

    private var savedInstanceState: Bundle? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    companion object {
        fun setIntent(context: Context, challenge: Challenge): Intent {
            val intent = Intent(context, DetailDebatActivity::class.java)
            intent.putExtra(EXTRA_CHALLENGE_ITEM, challenge)
            return intent
        }

        fun setIntent(context: Context, challengeId: String): Intent {
            val intent = Intent(context, DetailDebatActivity::class.java)
            intent.putExtra(EXTRA_CHALLENGE_ID, challengeId)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(EXTRA_CHALLENGE_ITEM)?.let {
            challenge = it as Challenge
            challengeId = it.id
        }
        intent.getStringExtra(EXTRA_CHALLENGE_ID)?.let { challengeId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        fl_progress_bar.visibleIf(challenge == null)

        challenge?.let { showChallenge(it) } ?: challengeId?.let { presenter.getChallengeItem(it) }

        btn_back.setOnClickListener { onBackPressed() }

        swipe_refresh.setOnRefreshListener {
            reloadChallenge()
        }
    }

    override fun showLoading() {
        fl_progress_bar.visibleIf(true)
        view_fail_state.visibleIf(false)
    }

    override fun dismissLoading() {
        fl_progress_bar.visibleIf(false)
        if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
    }

    override fun showChallenge(challenge: Challenge) {
        this.challenge = challenge
        isMyChallenge = challenge.challenger.userId == presenter.getMyProfile().id
        setupHeader()
        setupContent()
        setupDetail()
    }

    override fun onErrorGetChallenge(t: Throwable) {
        view_fail_state.visibleIf(true)
    }

    private fun setupHeader() {
        tv_toolbar_title.text = if (challenge?.status == Status.DONE) {
            Status.DONE
        } else {
            when (challenge?.type) {
                ChallengeConstants.Type.OPEN_CHALLENGE -> Status.OPEN_CHALLENGE
                ChallengeConstants.Type.DIRECT_CHALLENGE -> Status.DIRECT_CHALLENGE
                else -> ""
            }
        }
        tv_denied_debat.visibleIf(challenge?.status in arrayOf(Status.DENIED, Status.EXPIRED))
        tv_denied_debat.apply { if (isVisible()) text = challenge?.status }

        tv_name_challenger.text = challenge?.challenger?.fullName
        iv_avatar_challenger.loadUrl(challenge?.challenger?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_username_challenger.text = "@${challenge?.challenger?.username}"

        val opponent = challenge?.opponent ?: challenge?.opponentCandidates?.firstOrNull()
        val showAvatarOpponent = opponent != null && challenge?.status !in arrayOf(Status.DENIED, Status.EXPIRED)
        iv_avatar_opponent.visibleIf(showAvatarOpponent)
        ll_text_opponent.visibleIf(challenge?.opponent != null)
        if (ll_text_opponent.isVisible()) {
            tv_name_opponent.text = challenge?.opponent?.fullName
            tv_username_opponent.text = "@${challenge?.opponent?.username}"
        }

        val avatarOpponent = challenge?.opponent?.avatar?.thumbnailSquare?.url
            ?: challenge?.opponentCandidates?.firstOrNull()?.avatar?.thumbnailSquare?.url
        iv_avatar_opponent.apply { if (isVisible() && challenge?.opponentCandidates != null) {
            loadUrl(avatarOpponent, R.drawable.ic_avatar_placeholder)
            val count = when {
                challenge?.opponentCandidates?.size == 1 -> "?"
                challenge?.opponentCandidates!!.size > 1 -> challenge?.opponentCandidates?.size.toString()
                else -> null
            }
            tv_opponent_count.text = count
            tv_opponent_count.visibleIf(count != null)
        } }

        btn_more.setOnClickListener {
            val showDeleteButton = isMyChallenge && challenge?.progress == ChallengeConstants.Progress.WAITING_OPPONENT
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

        val bgToolbarColor = color(when (challenge?.status) {
            Status.COMING_SOON -> R.color.blue_2
            Status.DONE -> R.color.purple_5
            in arrayOf(Status.OPEN_CHALLENGE, Status.DIRECT_CHALLENGE, Status.EXPIRED, Status.DENIED) -> R.color.orange_3
            else -> throw ErrorException("Status debat tidak sesuai")
        })

        val bgHeader = when (challenge?.status) {
            Status.COMING_SOON -> R.drawable.banner_coming_soon_fullheight
            Status.DONE -> R.drawable.banner_done_post
            in arrayOf(Status.OPEN_CHALLENGE, Status.DIRECT_CHALLENGE, Status.EXPIRED, Status.DENIED) -> R.drawable.banner_challenge_big
            else -> throw ErrorException("Status debat tidak sesuai")
        }

        toolbar_detail_debat.setBackgroundColor(bgToolbarColor)
        cl_header.setBackgroundResource(bgHeader)

        btn_like.visibleIf(challenge?.status == Status.DONE)
        if (btn_like.isVisible()) {
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
        }
    }

    /* TODO: UBAH JADI FRAGMENT */
    private fun setupContent() {
        ll_content_detail_debat.apply {
            if (childCount > 2) {
                (this as ViewGroup).removeViewsInLayout(2, childCount - 2)
            }
        }
        when (challenge?.status) {
            Status.COMING_SOON -> onComingSoon()
            Status.DONE -> onDone()
            Status.OPEN_CHALLENGE -> onOpenChallenge()
            Status.DIRECT_CHALLENGE -> onDirectChallenge()
            Status.DENIED -> onDenied()
            Status.EXPIRED -> onExpired()
        }
    }

    private fun setupDetail() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_detail_debat))
        findViewById<TextView>(R.id.tv_label_detail).text = challenge?.topicList?.firstOrNull()

        val ll_webview = findViewById<LinearLayout>(R.id.ll_webview)
        val tv_statement_url = findViewById<TextView>(R.id.tv_statement_url)
        val tv_title_detail = findViewById<TextView>(R.id.tv_title_detail)
        val cl_opponent_detail = findViewById<ConstraintLayout>(R.id.cl_opponent_detail)
        val iv_opponent_avatar = findViewById<RoundedImageView>(R.id.iv_opponent_avatar)
        val tv_opponent_name = findViewById<TextView>(R.id.tv_opponent_name)
        val tv_opponent_username = findViewById<TextView>(R.id.tv_opponent_username)
        val tv_date_detail = findViewById<TextView>(R.id.tv_date_detail)
        val tv_hour_detail = findViewById<TextView>(R.id.tv_hour_detail)
        val tv_saldo_waktu_detail = findViewById<TextView>(R.id.tv_saldo_waktu_detail)
        val iv_creator_avatar_detail = findViewById<RoundedImageView>(R.id.iv_creator_avatar_detail)
        val tv_creator_name_detail = findViewById<TextView>(R.id.tv_creator_name_detail)
        val tv_creator_bio_detail = findViewById<TextView>(R.id.tv_creator_bio_detail)
        val tv_posted_time_detail = findViewById<TextView>(R.id.tv_posted_time_detail)
        val tv_label_saldo_waktu_desc = findViewById<TextView>(R.id.tv_label_saldo_waktu_desc)
        challenge?.statementSource?.let { ll_webview.visibleIf(it.isNotEmpty()) }
        if (ll_webview.isVisible()) {
            challenge?.statementSource?.let { presenter.getTweetPreview(it) }
            tv_statement_url.text = challenge?.statementSource
        }

        tv_title_detail.text = challenge?.statement

        cl_opponent_detail.visibleIf(challenge?.opponent != null || challenge?.type == ChallengeConstants.Type.DIRECT_CHALLENGE)
        if (cl_opponent_detail.isVisible()) {
            val opponent = challenge?.opponent ?: challenge?.opponentCandidates?.firstOrNull()
            iv_opponent_avatar.loadUrl(opponent?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
            opponent?.fullName?.let {
                tv_opponent_name.text = it
                tv_opponent_username.text = opponent.username.let { username -> if (username.startsWith("@")) username else "@$username" }
            } ?: run {
                tv_opponent_name.text = opponent?.username?.let { username -> if (username.startsWith("@")) username else "@$username" }
            }
        }

        tv_date_detail.text = challenge?.showTimeAt?.parseDate(toFormat = "EEEE, dd MMMM yyyy")
        tv_hour_detail.text = challenge?.showTimeAt?.parseDate(toFormat = "HH.mm")

        tv_saldo_waktu_detail.text = "${challenge?.timeLimit} menit"

        iv_creator_avatar_detail.loadUrl(challenge?.challenger?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_creator_name_detail.text = challenge?.challenger?.fullName
        tv_creator_bio_detail.text = challenge?.challenger?.about

        tv_posted_time_detail.text = "Posted in ${challenge?.createdAt?.parseDate(toFormat = "dd MMM yy HH:mm")}"

        val meAsOpponent = challenge?.opponent?.userId == presenter.getMyProfile().id
        tv_label_saldo_waktu_desc.text = when {
            isMyChallenge -> getString(R.string.saldo_waktu_desc)
            meAsOpponent -> getString(R.string.saldo_waktu_desc)
            else -> "Peserta debat mendapat saldo waktu yang sama:"
        }
    }

    override fun showLoadingUrlPreview() {
        findViewById<ProgressBar?>(R.id.progress_bar_url_preview)?.visibleIf(true)
    }

    override fun dismissLoadingUrlPreview() {
        findViewById<ProgressBar?>(R.id.progress_bar_url_preview)?.visibleIf(false)
    }

    override fun showTweetPreview(oEmbedLink: OEmbedLink) {
        val ll_webview = findViewById<LinearLayout?>(R.id.ll_webview)
        oEmbedLink.html?.let { ll_webview?.previewTweet(it) }
    }

    override fun showUrlPreview(urlItem: UrlItem) {
        val ll_webview = findViewById<LinearLayout?>(R.id.ll_webview)
        ll_webview?.previewUrl(urlItem)
    }

    override fun onErrorUrlPreview(t: Throwable) {
//        findViewById<WebView?>(R.id.ll_webview)?.visibleIf(false)
    }

    private fun onComingSoon() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_coming_soon))
        findViewById<TextView>(R.id.tv_content_subtitle).text = "Debat akan berlangsung ${challenge?.showTimeAt?.parseDateRemaining()} lagi!"
    }

    private fun onDone() {
        val challenger = challenge?.challenger
        val opponent = challenge?.opponent

        ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_done))
        findViewById<RoundedImageView>(R.id.iv_avatar_challenger_content).loadUrl(challenger?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)

        findViewById<RoundedImageView>(R.id.iv_avatar_opponent_content).loadUrl(opponent?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)

        findViewById<MaterialButton>(R.id.btn_lihat_debat).setOnClickListener { challenge?.let { startActivity(DebatActivity.setIntent(this, it)) } }
    }

    private fun onOpenChallenge() {
        if (isMyChallenge) {
            when (challenge?.progress) {
                Progress.WAITING_CONFIRMATION -> {
                    ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_accept_confirmation_as_challenger))
                    setupOpponentCandidateList()
                }
                Progress.WAITING_OPPONENT -> {
                    ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_waiting_opponent_as_challenger))
                }
            }
        } else {
            var meAsOpponentCandidate = false
            challenge?.opponentCandidates?.forEach {
                if (it.userId == presenter.getMyProfile().id) {
                    meAsOpponentCandidate = true
                    return@forEach
                }
            }
            if (meAsOpponentCandidate) {
                ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_waiting_confirmation_as_others))
                findViewById<TextView>(R.id.tv_content_subtitle).text = "${challenge?.challenger?.fullName} untuk konfirmasi lawan debat"
                setupOpponentCandidateList()
            } else {
                ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_accept_confirmation_as_opponent))
                val message = spannable {
                    + "Kamu akan menerima tantangan dari "
                    textColor(color(R.color.black)) { +"@${challenge?.challenger?.username} " }
                    + "untuk berdebat sesuai dengan detail yang tertera. Tindakan ini tidak bisa dibatalkan. Apakah kamu yakin?"
                }.toCharSequence()
                findViewById<MaterialButton>(R.id.btn_terima).setOnClickListener {
                    TerimaChallengeDialog(this@DetailDebatActivity, message,
                        "YA",
                        object : TerimaChallengeDialog.DialogListener {
                            override fun onClickTerima() {
                                challenge?.id?.let { presenter.askAsOpponent(it) }
                            }
                        }).show()
                }
            }
        }
    }

    private fun onDirectChallenge() {
        if (challenge?.opponentCandidates?.firstOrNull()?.userId == presenter.getMyProfile().id) {
            ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_direct_accept_confirmation_as_opponent))
            val message = spannable {
                + "Kamu akan menerima Direct Challenge dari "
                textColor(color(R.color.black)) { +"@${challenge?.challenger?.username} " }
                + "untuk berdebat"
            }.toCharSequence()
            findViewById<MaterialButton>(R.id.btn_terima).setOnClickListener {
                TerimaChallengeDialog(this@DetailDebatActivity, message,
                    listener = object : TerimaChallengeDialog.DialogListener {
                        override fun onClickTerima() {
                            challenge?.id?.let { presenter.approveDirect(it) }
                        }
                    }).show()
            }
            findViewById<MaterialButton>(R.id.btn_tolak).setOnClickListener {
                TolakChallengeDialog(this@DetailDebatActivity, challenge?.challenger?.username,
                    object : TolakChallengeDialog.DialogListener {
                        override fun onClickTolak(reason: String) {
                            challenge?.id?.let { presenter.rejectDirect(it, reason) }
                        }
                    }).show()
            }
        } else {
            ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_direct_waiting_confirmation))
            val opponent = challenge?.opponentCandidates?.firstOrNull()
            findViewById<TextView>(R.id.tv_content_subtitle).text = "${opponent?.fullName ?: opponent?.username ?: "lawan"} mengkonfirmasi tantangan"
        }
    }

    private fun onDenied() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_direct_rejected))
        val opponent = challenge?.opponentCandidates?.firstOrNull()
        findViewById<RoundedImageView>(R.id.iv_avatar).loadUrl(opponent?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        findViewById<TextView>(R.id.tv_name).text = opponent?.fullName
        findViewById<TextView>(R.id.tv_username).text = opponent?.username
        findViewById<TextView>(R.id.tv_content_reject_description)
            .text = challenge?.reasonRejected
            ?: "${challenge?.opponentCandidates?.firstOrNull()?.fullName} tidak menulis alasan/pernyataan"
    }

    private fun onExpired() {
        if (challenge?.type == ChallengeConstants.Type.OPEN_CHALLENGE) {
            ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_expired))
            setupOpponentCandidateList()
        } else {
            ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_expired))
        }
    }

    private fun setupOpponentCandidateList() {
        val adapter = OpponentCandidateAdapter(isMyChallenge, challenge?.status != Status.EXPIRED)
        val recycler_view = findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        challenge?.opponentCandidates?.let { adapter.addData(it) }
        if (isMyChallenge) {
            adapter.listener = object : OpponentCandidateAdapter.AdapterListener {
                override fun onClickConfirm(audience: Audience) {
                    val message = spannable {
                        textColor(color(R.color.gray_12)) { + "Kamu akan mengkonfirmasi " }
                        textColor(color(R.color.black)) { +"@${audience.username} " }
                        textColor(color(R.color.gray_12)) { + "sebagai lawan debat anda sesuai dengan detail yang tertera. Tindakan ini tidak bisa dibatalkan. Apakah kamu yakin?" }
                    }.toCharSequence()
                    TerimaChallengeDialog(this@DetailDebatActivity, message,
                        "YA",
                        object : TerimaChallengeDialog.DialogListener {
                            override fun onClickTerima() {
                                challenge?.id?.let { presenter.confirmOpponentCandidate(it, audience.id) }
                            }
                        }).show()
                }
            }
        }
    }

    /* ConfirmOpponentCandidate View */
    override fun showLoadingConfirmOpponentCandidate() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        showProgressDialog()
    }

    override fun dismissLoadingConfirmOpponentCandidate() {
        dismissProgressDialog()
    }

    override fun onSuccessConfirmOpponentCandidate(audienceId: String) {
        reloadChallenge()
    }

    override fun onErrorConfirmOpponentcandidate(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /* AskAsOpponent View */
    override fun showLoadingAskAsOpponent() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        showProgressDialog()
    }

    override fun dismissLoadingAskAsOpponent() {
        dismissProgressDialog()
    }

    override fun onSuccessAskAsOpponent() {
        reloadChallenge()
    }

    override fun onErrorAskAsOpponent(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingApproveDirect() {
        showProgressDialog()
    }

    override fun dismissLoadingApproveDirect() {
        dismissProgressDialog()
    }

    override fun onSuccessApproveDirect() {
        reloadChallenge()
    }

    override fun onErrorApproveDirect(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingRejectDirect() {
        showProgressDialog()
    }

    override fun dismissLoadingRejectDirect() {
        dismissProgressDialog()
    }

    override fun onSuccessRejectDirect() {
        reloadChallenge()
    }

    override fun onErrorRejectDirect(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun reloadChallenge() {
        challenge = null
        setupUI(savedInstanceState)
    }
}