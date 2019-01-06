package com.pantaubersama.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.login.LoginActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity<SplashScreenPresenter>(), SplashScreenView {
    @Inject
    lateinit var interactor: LoginInteractor

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): SplashScreenPresenter? {
        return SplashScreenPresenter(interactor)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        presenter?.getLoginState()
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
        (application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}
