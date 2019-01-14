package com.pantaubersama.app.ui.profile.connect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.* // ktlint-disable
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import com.twitter.sdk.android.core.* // ktlint-disable
import com.twitter.sdk.android.core.identity.TwitterAuthClient
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
        setupTwitterLogin()
    }

    private fun setupTwitterLogin() {
        Twitter.initialize(
            TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(getString(R.string.twitter_api_key), getString(R.string.twitter_secret_key)))
                .debug(true)
                .build()
        )
        twitterAuthClient = TwitterAuthClient()
        connect_twitter.setOnClickListener {
            twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    presenter.connectTwitter(PantauConstants.CONNECT.TWITTER, result.data.authToken.token, result.data.authToken.secret)
                }

                override fun failure(exception: TwitterException?) {
                    Timber.e(exception)
                    ToastUtil.show(this@ConnectActivity, "Gagal login dengan Twitter")
                }
            })
        }
    }

    override fun showConnectedToTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Terhubung dengan Twitter")
    }

    override fun showFailedToConnectTwitterAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal menghubungkan ke Twitter")
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
                // not implemented yet
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
                ToastUtil.show(this@ConnectActivity, "Anda sudah terhubung ke Facebook")
            }
        }
    }

    private fun getFacebookLoginSatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { me, response ->
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
}
