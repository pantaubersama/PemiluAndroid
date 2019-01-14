package com.pantaubersama.app.ui.profile.connect

import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_connect.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ConnectActivity : BaseActivity<ConnectPresenter>(), ConnectView {
    private lateinit var callbackManager: CallbackManager
    private lateinit var permissions: MutableList<String>

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
        AppEventsLogger.activateApp(this)
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
            LoginManager.getInstance().logInWithReadPermissions(this@ConnectActivity, permissions)
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
    }

    override fun showFailedToConnectFacebookAlert() {
        ToastUtil.show(this@ConnectActivity, "Gagal menghubungkan ke Facebook")
    }
}
