package com.pantaubersama.app.ui.debat.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.parseDate
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.previewTweet
import com.pantaubersama.app.utils.previewUrl
import kotlinx.android.synthetic.main.layout_detail_debat.*
import javax.inject.Inject

/**
 * @author edityomurti on 21/02/2019 13:26
 */
class DetailDebatDialogFragment : BaseDialogFragment<DetailDebatDialogPresenter>(), DetailDebatDialogView {
    override fun setLayout(): Int = R.layout.layout_detail_debat

    private lateinit var challenge: Challenge

    @Inject
    override lateinit var presenter: DetailDebatDialogPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        private val TAG = DetailDebatDialogFragment::class.java.simpleName

        fun show(fragmentManager: FragmentManager, challenge: Challenge) {
            val dialog = DetailDebatDialogFragment()
            val args = Bundle()
            args.putSerializable(EXTRA_CHALLENGE_ITEM, challenge)
            dialog.arguments = args
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun fetchArguments(args: Bundle?) {
        arguments?.getSerializable(EXTRA_CHALLENGE_ITEM)?.let { challenge = it as Challenge }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        tv_label_detail.text = challenge.topicList.firstOrNull()

        ll_webview.visibleIf(challenge.statementSource.isNotEmpty())
        if (ll_webview.isVisible()) {
            presenter.getTweetPreview(challenge.statementSource)
            tv_statement_url.text = challenge.statementSource
        }

        tv_title_detail.text = challenge.statement

        val opponent = challenge.opponent
        iv_opponent_avatar.loadUrl(opponent?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        opponent?.fullName?.let {
            tv_opponent_name.text = it
            tv_opponent_username.text = opponent.username.let { username -> if (username.startsWith("@")) username else "@$username" }
        } ?: run {
            tv_opponent_name.text = opponent?.username?.let { username -> if (username.startsWith("@")) username else "@$username" }
        }

        tv_date_detail.text = challenge.showTimeAt.parseDate(toFormat = "EEEE, dd MMMM yyyy")
        tv_hour_detail.text = challenge.showTimeAt.parseDate(toFormat = "HH.mm")

        tv_saldo_waktu_detail.text = "${challenge?.timeLimit} menit"

        iv_creator_avatar_detail.loadUrl(challenge.challenger.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_creator_name_detail.text = challenge.challenger.fullName
        tv_creator_bio_detail.text = challenge.challenger.about

        tv_posted_time_detail.text = "Posted in ${challenge?.createdAt?.parseDate(toFormat = "dd MMM yy HH:mm")}"

        val isMyChallenge = presenter.getMyProfile().id in arrayOf(challenge.challenger.userId, challenge.opponent?.userId)
        tv_label_saldo_waktu_desc.text = when {
            isMyChallenge -> getString(R.string.saldo_waktu_desc)
            else -> "Peserta debat mendapat saldo waktu yang sama:"
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingUrlPreview() {
        progress_bar_url_preview.visibleIf(true)
    }

    override fun dismissLoadingUrlPreview() {
        progress_bar_url_preview.visibleIf(false)
    }

    override fun showTweetPreview(oEmbedLink: OEmbedLink) {
        oEmbedLink.html?.let { ll_webview.previewTweet(it) }
    }

    override fun showUrlPreview(urlItem: UrlItem) {
        ll_webview?.previewUrl(urlItem)
    }

    override fun onErrorUrlPreview(t: Throwable) {
//        ll_webview?.visibleIf(false)
    }
}