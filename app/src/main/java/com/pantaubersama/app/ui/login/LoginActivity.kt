package com.pantaubersama.app.ui.login

import android.content.Intent
import com.extrainteger.identitaslogin.Callback
import com.extrainteger.identitaslogin.Result
import com.extrainteger.identitaslogin.SymbolicConfig
import com.extrainteger.identitaslogin.SymbolicException
import com.extrainteger.identitaslogin.models.AuthToken
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {
    @Inject
    lateinit var rxSchedulers: RxSchedulers
    private var symbolicScope: MutableList<String>? = null

    override fun statusBarColor(): Int {
        return R.color.colorPrimaryDark
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun setupUI() {
        symbolicScope = ArrayList()
        symbolic_login_button.configure(
            SymbolicConfig(
                this@LoginActivity,
                "https://identitas.extrainteger.com",
                "ad68192bfcfe8085492dc82af35e26c8ca92a8d08db9e9d7820e054a849d5add",
                "bd09ebed07d20d7b438d96d7f8507922b25511887781740bbbb7ddb9a1055469",
                "com.pantaubersama.app://oauth",
                symbolicScope
                )
        )
        symbolic_login_button.setCallback(object : Callback<AuthToken>() {
            override fun failure(exception: SymbolicException) {
                ToastUtil.show(this@LoginActivity, "Failed to login using Symbolic")
            }

            override fun success(result: Result<AuthToken>) {
                presenter?.exchangeToken(result.data.accessToken, "")
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

    override fun initPresenter(): LoginPresenter? {
        return LoginPresenter(rxSchedulers)
    }

    override fun showLoading() {
        showProgressDialog("Logging in")
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
