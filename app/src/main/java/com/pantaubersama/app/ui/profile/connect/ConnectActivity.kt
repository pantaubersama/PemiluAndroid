package com.pantaubersama.app.ui.profile.connect

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.CookieManager
import androidx.core.content.ContextCompat
import com.facebook.* // ktlint-disable
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import com.twitter.sdk.android.core.* // ktlint-disable
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_connect.* // ktlint-disable
import timber.log.Timber
import javax.inject.Inject

class ConnectActivity : BaseActivity<ConnectPresenter>(), ConnectView {
    private lateinit var callbackManager: CallbackManager
    private lateinit var permissions: MutableList<String>
    private lateinit var twitterAuthClient: TwitterAuthClient

    @Inject
    override lateinit var presenter: ConnectPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(false, getString(R.string.title_connet), R.color.white, 4f)
        getFacebookLoginSatus()
        setupFacebookLogin()
        getTwitterUserData()
        setupTwitterLogin()
        connect_submit.setOnClickListener {
            openHomeActivity()
        }
    }

    private fun getTwitterUserData() {
        if (TwitterCore.getInstance().sessionManager.activeSession != null) {
            presenter.getTwitterUserData()
            connect_twitter_label.text = getString(R.string.label_connected_as)
        } else {
            connect_twitter_label.text = getString(R.string.label_click_connect_twitter)
            twitter_login_text.text = getString(R.string.label_connect_with_twitter)
            twitter_login_icon.setImageDrawable(ContextCompat.getDrawable(this@ConnectActivity, R.drawable.ic_twitter))
        }
    }

    override fun bindTwitterUserData(data: User?) {
        data?.screenName?.let { twitter_login_text.text = it }
        data?.profileImageUrlHttps?.let { twitter_login_icon.loadUrl(it) }
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal memuat data pengguna Twitter")
    }

    private fun setupTwitterLogin() {
        twitterAuthClient = TwitterAuthClient()
        setting_connect_twitter.setOnClickListener {
            if (TwitterCore.getInstance().sessionManager.activeSession == null) {
                twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
                    override fun success(result: Result<TwitterSession>) {
                        presenter.connectTwitter(PantauConstants.CONNECT.TWITTER, result.data.authToken.token, result.data.authToken.secret)
                    }

                    override fun failure(exception: TwitterException?) {
                        Timber.e(exception)
                        ToastUtil.show(this@ConnectActivity, "Gagal login dengan Twitter")
                    }
                })
            } else {
                ConfirmationDialog
                    .Builder()
                    .with(this@ConnectActivity)
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

    override fun showConnectedToTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Terhubung dengan Twitter")
        getTwitterUserData()
    }

    override fun showFailedToConnectTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal menghubungkan ke Twitter")
    }

    private fun getFacebookLoginSatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            facebook_connect_label.text = getString(R.string.label_connected_as)
            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { me, _ ->
                facebook_login_text.text = me?.getString("name")
                val request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + me.getString("id") + "/picture"
                ) {
                    try {
                        facebook_login_icon.loadUrl(it.jsonObject.getJSONObject("data").getString("url"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val params = Bundle()
                params.putBoolean("redirect", false)
                request.parameters = params
                request.executeAsync()
            }
            request.executeAsync()
        } else {
            facebook_connect_label.text = getString(R.string.label_click_connect_facebook)
            facebook_login_text.text = getString(R.string.label_connect_with_fb)
            facebook_login_icon.setImageDrawable(ContextCompat.getDrawable(this@ConnectActivity, R.drawable.facebook))
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
                ToastUtil.show(this@ConnectActivity, "Gagal mengubungkan ke Facebook")
            }
        })
        connect_fb.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                LoginManager.getInstance().logInWithReadPermissions(this@ConnectActivity, permissions)
            } else {
                ConfirmationDialog
                    .Builder()
                    .with(this@ConnectActivity)
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

    override fun setLayout(): Int {
        return R.layout.activity_connect
    }

    override fun showLoading() {
        showProgressDialog("Mohon tunggu")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showConnectedToFacebookAlert() {
        ToastUtil.show(this@ConnectActivity, "Terhubung dengan Facebook")
        getFacebookLoginSatus()
    }

    override fun showFailedToConnectFacebookAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal menghubungkan ke Facebook")
    }

    override fun showSuccessDisconnectFacebookAlert() {
        ToastUtil.show(this@ConnectActivity, "Disconnect Facebook berhasil")
    }

    override fun showSuccessDisconnectTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Disconnect Twitter berhasil")
    }

    override fun showFailedDisconnectFacebookAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal disconnect Facebook")
    }

    override fun showFailedDisconnectTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal disconnect Twitter")
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

    override fun onBackPressed() {
        openHomeActivity()
    }

    private fun openHomeActivity() {
        val intent = Intent(this@ConnectActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }
}
