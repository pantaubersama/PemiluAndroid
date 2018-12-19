package com.pantaubersama.app.ui.splashscreen

import android.content.Intent
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.login.LoginActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), SplashScreenView {
    @Inject
    lateinit var presenter: SplashScreenPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        presenter.attach(this)
        presenter.getLoginState()
    }

    override fun goToHome() {
        val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun goToLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun setLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        presenter.detach()
        (application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}
