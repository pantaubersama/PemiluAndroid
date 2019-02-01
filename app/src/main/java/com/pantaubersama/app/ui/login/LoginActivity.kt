package com.pantaubersama.app.ui.login

import android.content.Intent
import android.os.Bundle
import com.extrainteger.symbolic.Callback
import com.extrainteger.symbolic.Result
import com.extrainteger.symbolic.SymbolicConfig
import com.extrainteger.symbolic.SymbolicException
import com.extrainteger.symbolic.models.SymbolicToken
import com.extrainteger.symbolic.ui.SymbolicLoginButton
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Companion.CONFIRMATION_PATH
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {
    @Inject
    override lateinit var presenter: LoginPresenter
    private var symbolicScope: MutableList<String>? = null
    private var url: String? = null

    override fun statusBarColor(): Int {
        return R.color.colorPrimaryDark
    }

    override fun fetchIntentExtra() {
        intent?.let { url = it.getStringExtra(PantauConstants.URL) }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        if (!url.isNullOrEmpty()) {
            if (url?.contains(PantauConstants.Networking.INVITATION_PATH)!!) {
                url?.let { SymbolicLoginButton.loadPage(this@LoginActivity, it) }
            } else if (url?.contains(CONFIRMATION_PATH)!!) {
                url?.let { SymbolicLoginButton.loadPage(this@LoginActivity, it) }
            }
        }
        symbolicScope = ArrayList()
        symbolic_login_button.configure(
            SymbolicConfig(
                this@LoginActivity,
                BuildConfig.SYMBOLIC_URL,
                BuildConfig.SYMBOLIC_CLIENT_ID,
                BuildConfig.SYMBOLIC_CLIENT_SECRET,
                BuildConfig.SYMBOLIC_REDIRECT_URI,
                symbolicScope
            )
        )
        symbolic_login_button.setCallback(object : Callback<SymbolicToken>() {

            override fun success(result: Result<SymbolicToken>) {
                presenter.exchangeToken(result.data.accessToken)
            }

            override fun failure(exception: SymbolicException) {
                ToastUtil.show(this@LoginActivity, getString(R.string.failed_login_alert))
            }
        })
        lewati_button.setOnClickListener {
            openHomeActivity()
        }
    }

    override fun onSuccessLogin() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d(task.result?.token)
                    task.result?.token?.let { presenter.saveFirebaseKey(it) }
                } else {
                    task.exception?.let { showError(it) }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        symbolic_login_button.onActivityResult(requestCode, resultCode, data)
    }

    override fun setLayout(): Int {
        return R.layout.activity_login
    }

    override fun showLoading() {
        showProgressDialog(getString(R.string.logging_in_label))
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun onSuccessGetProfile(it: Profile?) {
        FirebaseMessaging.getInstance().subscribeToTopic("broadcasts-activity")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Subscribing to broadcast channel")
                } else {
                    Timber.d("Failed to subscribe to broadcast channel")
                }
            }
        if (it?.username != null &&
            it.education != null &&
            it.location != null &&
            it.occupation != null) {
            openHomeActivity()
        } else {
            openEditProfileActivity()
        }
    }

    private fun openEditProfileActivity() {
        val intent = Intent(this@LoginActivity, EditProfileActivity::class.java)
        intent.putExtra(PantauConstants.PROFILE_COMPLETION, true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun openHomeActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun showLoginFailedAlert() {
        ToastUtil.show(this@LoginActivity, getString(R.string.login_failed_alert))
    }
}
