package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.CookieManager
import androidx.core.content.ContextCompat
import com.facebook.* // ktlint-disable
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.Challenge
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.ui.wordstadium.InfoDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import com.twitter.sdk.android.core.* // ktlint-disable
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_promote_challenge.*
import timber.log.Timber
import javax.inject.Inject
import android.content.ComponentName
import android.net.Uri
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_DATE_STRING
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OEMBEDED_LINK
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_URL_ITEM
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.previewTweet
import com.pantaubersama.app.utils.previewUrl

class PromoteChallengeActivity : BaseActivity<PromoteChallengePresenter>(), PromoteChallengeView {

    lateinit var challenge: Challenge
    private lateinit var callbackManager: CallbackManager
    private lateinit var permissions: MutableList<String>
    private lateinit var twitterAuthClient: TwitterAuthClient
    var date: String? = null
    var fbConnected = false
    var oEmbedLink: OEmbedLink? = null
    var urlItem: UrlItem? = null

    @Inject
    override lateinit var presenter: PromoteChallengePresenter

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
        return R.layout.activity_promote_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_promote_challenge), R.color.white, 4f)
        presenter.getUserProfile()
        setDetailChallenge()
        publishButton(false)
        setupFacebookLogin()
        setupTwitterLogin()
        promote_challenge_publish.setOnClickListener {
            presenter.openChallenge(challenge.bidangKajian, challenge.pernyataan, challenge.link, date, challenge.saldoWaktu)
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
        promote_challenge_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        promote_challenge_avatar_2.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        promote_challenge_name.text = profile.fullName
        promote_challenge_username.text = profile.username
        promote_challenge_username_2.text = profile.username
    }

    override fun onSuccessOpenChallenge() {
        val infoDialog = InfoDialog.newInstance(R.layout.info_success_open_challenge)
        infoDialog.show(supportFragmentManager, "success-open")

        Handler().postDelayed({
            setResult(Activity.RESULT_OK)
            finish()
            if (fbConnected)
                shareFacebook("Berhasil membuat Tantangan Debat di Pantau Bersama...siapa yang berani menajadi lawannya??? #PantauBersama", "https://pantaubersama.com")
        }, 3000)
    }

    fun setDetailChallenge() {
        promote_challenge_bid_kajian.text = challenge.bidangKajian
        promote_challenge_pernyataan.text = challenge.pernyataan
        promote_challenge_date.text = challenge.date
        promote_challenge_time.text = challenge.time
        promote_challenge_saldo_waktu.text = "${challenge.saldoWaktu}"
    }

    fun publishButton(isActive: Boolean) {
        promote_challenge_publish.enable(isActive)
        promote_challenge_publish.setBackgroundColor(
                ContextCompat.getColor(this, if (isActive) R.color.orange_2 else R.color.gray_dark_1))
    }

    private fun getFacebookLoginSatus() {
        with(AccessToken.getCurrentAccessToken()) {
            if (this != null) {
                val request = GraphRequest.newMeRequest(
                        this
                ) { me, _ ->
                    promote_challenge_ll_connect_fb.visibility = View.VISIBLE
                    promote_challenge_fb_name.text = me?.getString("name")
                    publishButton(true)
                    val request = GraphRequest.newGraphPathRequest(
                            this,
                            "/" + me.getString("id") + "/picture"
                    ) {
                    }
                    val params = Bundle()
                    params.putBoolean("redirect", false)
                    request.parameters = params
                    request.executeAsync()
                }
                request.executeAsync()
            } else {
                if (AccessToken.getCurrentAccessToken() == null) {
                    LoginManager.getInstance().logInWithReadPermissions(this@PromoteChallengeActivity, permissions)
                } else {
                    ConfirmationDialog
                            .Builder()
                            .with(this@PromoteChallengeActivity)
                            .setDialogTitle("Disconnect Facebook")
                            .setAlert("Apakah Anda yakin untuk disconnect Facebook?")
                            .setCancelText("Batal")
                            .setOkText("Disconnect")
                            .addOkListener(object : ConfirmationDialog.DialogOkListener {
                                override fun onClickOk() {
                                    presenter.disconnectSocialMedia(PantauConstants.CONNECT.FACEBOOK)
                                }
                            })
                            .show()
                }
            }
        }
    }

    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        permissions = ArrayList()
        permissions.add("public_profile")
        permissions.add("email")
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                presenter.connectFacebook(PantauConstants.CONNECT.FACEBOOK, result?.accessToken?.token)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Timber.e(error?.localizedMessage)
                ToastUtil.show(this@PromoteChallengeActivity, "Gagal mengubungkan ke Facebook")
            }
        })

        switch_fb.setOnCheckedChangeListener { _, isChecked ->
            fbConnected = isChecked
            if (isChecked) {
                getFacebookLoginSatus()
            } else {
                promote_challenge_ll_connect_fb.visibility = View.GONE
                publishButton(false)
            }
        }
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
                        ToastUtil.show(this@PromoteChallengeActivity, "Gagal login dengan Twitter")
                    }
                })
            } else {
                ConfirmationDialog
                        .Builder()
                        .with(this@PromoteChallengeActivity)
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
                promote_challenge_ll_connect_twitter.visibility = View.GONE
            }
        }
    }

    override fun showConnectedToFacebookAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Terhubung dengan Facebook")
        getFacebookLoginSatus()
    }

    override fun showFailedToConnectFacebookAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Gagal menghubungkan ke Facebook")
    }

    override fun showConnectedToTwitterAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Terhubung dengan Twitter")
        getTwitterUserData()
    }

    override fun showFailedToConnectTwitterAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Gagal menghubungkan ke Twitter")
    }

    override fun bindTwitterUserData(data: User?) {
        data?.screenName?.let { promote_challenge_twitter_name.text = "@" + it }
        promote_challenge_ll_connect_twitter.visibility = View.VISIBLE
        publishButton(true)
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Gagal memuat data pengguna Twitter")
    }

    override fun showSuccessDisconnectFacebookAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Disconnect Facebook berhasil")
    }

    override fun showSuccessDisconnectTwitterAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Disconnect Twitter berhasil")
    }

    override fun showFailedDisconnectFacebookAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Gagal disconnect Facebook")
    }

    override fun showFailedDisconnectTwitterAlert() {
        ToastUtil.show(this@PromoteChallengeActivity, "Gagal disconnect Twitter")
    }

    override fun logoutFacebookSDK() {
        CookieManager.getInstance().removeAllCookies(null)
        LoginManager.getInstance().logOut()
        getFacebookLoginSatus()
    }

    override fun logoutTwitterSDK() {
        CookieManager.getInstance().removeAllCookies(null)
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        getTwitterUserData()
    }

    override fun onFailedConnectFacebook() {
        logoutFacebookSDK()
    }

    override fun onFailedConnectTwitter() {
        logoutTwitterSDK()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun shareFacebook(text: String, url: String) {
        var facebookAppFound = false
        var shareIntent = Intent(android.content.Intent.ACTION_SEND)
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url))

        val pm = packageManager
        val activityList = pm.queryIntentActivities(shareIntent, 0)
        for (app in activityList) {
            if (app.activityInfo.packageName.contains("com.facebook.katana") || app.activityInfo.packageName.contains("com.facebook.lite")) {
                val activityInfo = app.activityInfo
                val name = ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                shareIntent.component = name
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$url"
            shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }
        startActivity(shareIntent)
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
