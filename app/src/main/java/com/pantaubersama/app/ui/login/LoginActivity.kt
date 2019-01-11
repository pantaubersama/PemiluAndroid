package com.pantaubersama.app.ui.login

import android.content.Intent
import android.os.Bundle
import com.extrainteger.identitaslogin.Callback
import com.extrainteger.identitaslogin.Result
import com.extrainteger.identitaslogin.SymbolicConfig
import com.extrainteger.identitaslogin.SymbolicException
import com.extrainteger.identitaslogin.models.AuthToken
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {

    @Inject
    override lateinit var presenter: LoginPresenter

    private var symbolicScope: MutableList<String>? = null

    override fun statusBarColor(): Int {
        return R.color.colorPrimaryDark
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
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
        symbolic_login_button.setCallback(object : Callback<AuthToken>() {
            override fun failure(exception: SymbolicException) {
                ToastUtil.show(this@LoginActivity, getString(R.string.failed_login_alert))
            }

            override fun success(result: Result<AuthToken>) {
                presenter.exchangeToken(result.data.accessToken, "")
            }
        })
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

    override fun openHomeActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun showLoginFailedAlert() {
        ToastUtil.show(this@LoginActivity, getString(R.string.login_failed_alert))
    }
}
