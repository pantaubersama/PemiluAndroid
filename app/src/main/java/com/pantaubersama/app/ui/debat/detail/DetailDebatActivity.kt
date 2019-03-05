package com.pantaubersama.app.ui.debat.detail

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Audience
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.TerimaChallengeDialog
import com.pantaubersama.app.ui.debat.adapter.OpponentCandidateAdapter
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.ui.widget.PreviewWebViewClient
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.parseDate
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_detail_debat.*
import kotlinx.android.synthetic.main.layout_content_detail_debat_open_accept_confirmation_as_challenger.*
import kotlinx.android.synthetic.main.layout_detail_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_centered_title.*
import java.util.*
import javax.inject.Inject

class DetailDebatActivity : BaseActivity<DetailDebatPresenter>(), DetailDebatView {
    private var isMyChallenge = false

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_detail_debat

    @Inject
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
        isMyChallenge = challenge.challenger.userId == presenter.getMyProfile().id
        setupHeader()
        setupContent()
        setupDetail()

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
        iv_avatar_challenger.loadUrl(challenge.challenger.avatar?.medium?.url, R.color.gray_3)
        tv_username_challenger.text = "@${challenge.challenger.username}"

        val showAvatarOpponent = challenge.progress != Progress.WAITING_OPPONENT && challenge.status !in arrayOf(Status.DENIED, Status.EXPIRED)
        iv_avatar_opponent.visibleIf(showAvatarOpponent)
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
            val showDeleteButton = isMyChallenge && challenge.progress == ChallengeConstants.Progress.WAITING_OPPONENT
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

        btn_like.visibleIf(challenge.status == Status.DONE)
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

    private fun setupContent() {
        when (challenge.status) {
            Status.COMING_SOON -> onComingSoon()
            Status.DONE -> onDone()
            Status.OPEN_CHALLENGE -> onOpenChallenge()
        }
    }

    private fun setupDetail() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_detail_debat))
        presenter.getStatementSourcePreview(challenge.statementSource)

        tv_label_detail.text = challenge.topicList.firstOrNull()
        tv_statement_url.text = challenge.statementSource
        tv_title_detail.text = challenge.statement

        cl_opponent_detail.visibleIf(challenge.opponent != null)
        if (cl_opponent_detail.isVisible()) {
            val opponent = challenge.opponent
            iv_opponent_avatar.loadUrl(opponent?.avatar?.medium?.url, R.color.gray_3)
            tv_opponent_name.text = opponent?.fullName
            tv_opponent_username.text = "@${opponent?.username}"
        }

        tv_date_detail.text = challenge.showTimeAt.parseDate(toFormat = "EEEE, dd MMMM yyyy")
        tv_hour_detail.text = challenge.showTimeAt.parseDate(toFormat = "hh.mm")

        tv_saldo_waktu_detail.text = "${challenge.timeLimit} menit"

        iv_creator_avatar_detail.loadUrl(challenge.challenger.avatar?.medium?.url, R.color.gray_3)
        tv_creator_name_detail.text = challenge.challenger.fullName
        tv_creator_bio_detail.text = challenge.challenger.about

        tv_posted_time_detail.text = "Posted in ${challenge.createdAt.parseDate(toFormat = "dd MMM yy hh:mm")}"
    }

    override fun showLoadingStatementSource() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoadingStatementSource() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorStatementSource(throwable: Throwable) {
        link_webview.visibleIf(false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun showStatementSource(url: String) {
        if (!link_webview.isVisible()) link_webview.visibleIf(true)
        link_webview.webViewClient = PreviewWebViewClient()
        link_webview.settings.loadsImagesAutomatically = true
        link_webview.settings.javaScriptEnabled = true
        link_webview.settings.setAppCacheEnabled(true)
        link_webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        link_webview.loadDataWithBaseURL("https://twitter.com", url, "text/html", "utf-8", "")
        link_webview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private fun onComingSoon() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_coming_soon))
    }

    private fun onDone() {
        ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_done))
    }

    private fun onOpenChallenge() {
        if (isMyChallenge) {
            when (challenge.progress) {
                Progress.WAITING_CONFIRMATION -> {
                    ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_accept_confirmation_as_challenger))
                    setupOpponentCandidateList()
                }
                Progress.WAITING_OPPONENT -> {
                    ll_content_detail_debat.addView(inflate(R.layout.layout_content_detail_debat_open_waiting_opponent_as_challenger))
                }
            }
        } else {
            if (challenge.progress != Progress.COMING_SOON) {
                var alreadyAcceptChallenge = false
                challenge.opponentCandidates.forEach {
                    if (it.id == presenter.getMyProfile().id) {
                        alreadyAcceptChallenge = true
                        return@forEach
                    }
                }
                if (alreadyAcceptChallenge) {
                } else {
                    ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_open_accept_confirmation_as_opponent, null))
                }
            }
        }
    }

    private fun setupOpponentCandidateList() {
        val adapter = OpponentCandidateAdapter(isMyChallenge)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter.addData(challenge.opponentCandidates)
        adapter.listener = object : OpponentCandidateAdapter.AdapterListener {
            override fun onClickConfirm(audience: Audience) {
                val message = spannable {
                    textColor(color(R.color.gray_12)) { + "Kamu akan mengkonfirmasi " }
                    textColor(color(R.color.black)) { +"@${audience.username} " }
                    textColor(color(R.color.gray_12)) { + "sebagai lawan debat anda sesuai dengan detail yang tertera. Tindakan ini tidak bisa dibatalkan. Apakah kamu yakin?"}
                }.toCharSequence()
                TerimaChallengeDialog(this@DetailDebatActivity, message.toString(),
                    "YA",
                    object : TerimaChallengeDialog.DialogListener {
                        override fun onClickTerima() {
                            presenter.confirmOpponentCandidate(challenge.id, audience.id)
                        }
                    }).show()
            }
        }
    }

    override fun showLoadingConfirmOpponentCandidate() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        showProgressDialog(getString(R.string.txt_mohon_tunggu))
    }

    override fun dismissLoadingConfirmOpponentCandidate() {
        dismissProgressDialog()
    }

    override fun onSuccessConfirmOpponentCandidate(audienceId: String) {
        val currentChallenge = challenge
        currentChallenge.progress = Progress.COMING_SOON
        currentChallenge.opponent = challenge.audiences.find { it.id == audienceId }
        finish()
        startActivity(DetailDebatActivity.setIntent(this, currentChallenge))
    }

    override fun onErrorConfirmOpponentcandidate(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}