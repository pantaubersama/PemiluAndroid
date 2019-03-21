package com.pantaubersama.app.ui.wordstadium.challenge.direct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.CookieManager
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.Challenge
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.ui.wordstadium.InfoDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_DATE_STRING
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OEMBEDED_LINK
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_URL_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.previewTweet
import com.pantaubersama.app.utils.previewUrl
import com.twitter.sdk.android.core.* // ktlint-disable
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_preview_challenge.*
import timber.log.Timber
import javax.inject.Inject

class PreviewChallengeActivity : BaseActivity<PreviewChallengePresenter>(), PreviewChallengeView {

    lateinit var challenge: Challenge
    private lateinit var permissions: MutableList<String>
    private lateinit var twitterAuthClient: TwitterAuthClient
    var date: String? = null
    var oEmbedLink: OEmbedLink? = null
    var urlItem: UrlItem? = null

    @Inject
    override lateinit var presenter: PreviewChallengePresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(EXTRA_CHALLENGE_ITEM).let { challenge = it as Challenge }
        intent.getStringExtra(EXTRA_DATE_STRING).let { date = it as String }
        intent.getSerializableExtra(EXTRA_OEMBEDED_LINK)?.let { oEmbedLink = it as OEmbedLink? }
            ?: intent.getSerializableExtra(EXTRA_URL_ITEM)?.let { urlItem = it as UrlItem }
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_preview_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_preview), R.color.white, 4f)
        presenter.getUserProfile()
        setDetailChallenge()
        publishButton(false)
        setupTwitterLogin()
        preview_publish.setOnClickListener {
            presenter.directChallenge(challenge.bidangKajian, challenge.pernyataan, challenge.link, date, challenge.saldoWaktu, challenge.invitationId, challenge.screenName)
        }

        previewLink()
    }

    override fun showLoading() {
        showProgressDialog("Mohon menunggu...")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun showProfile(profile: Profile) {
        preview_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        preview_avatar_2.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        preview_name.text = profile.fullName
        preview_username.text = profile.username
        preview_username_2.text = profile.username
    }

    override fun onSuccessOpenChallenge() {
        val infoDialog = InfoDialog.newInstance(R.layout.info_success_open_challenge)
        infoDialog.show(supportFragmentManager, "success-open")

        Handler().postDelayed({
            setResult(Activity.RESULT_OK)
            finish()
        }, 3000)
    }

    fun setDetailChallenge() {
        preview_bid_kajian.text = challenge.bidangKajian
        preview_pernyataan.text = challenge.pernyataan
        preview_date.text = challenge.date
        preview_time.text = challenge.time
        preview_saldo_waktu.text = "${challenge.saldoWaktu}"
        preview_ava_lawan_twitter.loadUrl(challenge.avatar, R.drawable.ic_avatar_placeholder)
        preview_name_lawan_twitter.text = challenge.name
        preview_username_lawan_twitter.text = challenge.screenName
    }

    fun publishButton(isActive: Boolean) {
        preview_publish.enable(isActive)
        preview_publish.setBackgroundColor(
                ContextCompat.getColor(this, if (isActive) R.color.orange_2 else R.color.gray_dark_1))
    }

    private fun getTwitterUserData() {
        if (TwitterCore.getInstance().sessionManager.activeSession != null) {
            presenter.getTwitterUserData()
        } else {
            if (TwitterCore.getInstance().sessionManager.activeSession == null) {
                twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
                    override fun success(result: Result<TwitterSession>) {
                        presenter.connectTwitter(PantauConstants.CONNECT.TWITTER, result.data.authToken.token, result.data.authToken.secret)
                    }

                    override fun failure(exception: TwitterException?) {
                        Timber.e(exception)
                        ToastUtil.show(this@PreviewChallengeActivity, "Gagal login dengan Twitter")
                    }
                })
            } else {
                ConfirmationDialog
                        .Builder()
                        .with(this@PreviewChallengeActivity)
                        .setDialogTitle("Disconnect Twitter")
                        .setAlert("Apakah Anda yakin untuk disconnect Twitter?")
                        .setCancelText("Batal")
                        .setOkText("Disconnect")
                        .addOkListener(object : ConfirmationDialog.DialogOkListener {
                            override fun onClickOk() {
                                presenter.disconnectSocialMedia(PantauConstants.CONNECT.TWITTER)
                            }
                        })
                        .show()
            }
        }
    }

    private fun setupTwitterLogin() {
        twitterAuthClient = TwitterAuthClient()
        switch_twitter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getTwitterUserData()
            } else {
                preview_ll_connect_twitter.visibility = View.GONE
            }
        }
    }

    override fun showConnectedToTwitterAlert() {
        ToastUtil.show(this@PreviewChallengeActivity, "Terhubung dengan Twitter")
        getTwitterUserData()
    }

    override fun showFailedToConnectTwitterAlert() {
        ToastUtil.show(this@PreviewChallengeActivity, "Gagal menghubungkan ke Twitter")
    }

    override fun bindTwitterUserData(data: User?) {
        data?.screenName?.let { preview_twitter_name.text = "@" + it }
        preview_ll_connect_twitter.visibility = View.VISIBLE
        publishButton(true)
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@PreviewChallengeActivity, "Gagal memuat data pengguna Twitter")
    }

    override fun showSuccessDisconnectTwitterAlert() {
        ToastUtil.show(this@PreviewChallengeActivity, "Disconnect Twitter berhasil")
    }

    override fun showFailedDisconnectTwitterAlert() {
        ToastUtil.show(this@PreviewChallengeActivity, "Gagal disconnect Twitter")
    }

    override fun logoutTwitterSDK() {
        CookieManager.getInstance().removeAllCookies(null)
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        getTwitterUserData()
    }

    override fun onFailedConnectTwitter() {
        logoutTwitterSDK()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun previewLink() {
        oEmbedLink?.html?.let {
            ll_webview.visibleIf(true)
            ll_webview.previewTweet(it)
            link_source.text = oEmbedLink?.url
        } ?: urlItem?.let {
            ll_webview.visibleIf(true)
            ll_webview.previewUrl(it)
            link_source.text = urlItem?.sourceUrl
        }
    }
}
