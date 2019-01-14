package com.pantaubersama.app.ui.splashscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_JANPOL_PATH
import timber.log.Timber
import javax.inject.Inject

class SplashScreenActivity : BaseActivity<SplashScreenPresenter>(), SplashScreenView {
    private var urlAction: String? = null
    private var urlData: String? = null
    private var urlPath: String? = null

    @Inject
    override lateinit var presenter: SplashScreenPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        if (intent != null) {
            urlAction = intent.action
            urlData = intent.data?.toString()
            urlPath = intent.data.path
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        presenter.getLoginState()
    }

    override fun goToHome() {
        when {
            urlPath.isNullOrEmpty() -> {
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in)
            }
            urlPath?.contains(SHARE_JANPOL_PATH)!! -> {
                Timber.d("urlPath path == $urlPath")
                val janpolId = urlPath?.substringAfter(SHARE_JANPOL_PATH)
                Timber.d("janpol id == $urlPath")
            }
            urlPath != null -> {
                val intent = Intent(this@SplashScreenActivity, ProfileActivity::class.java)
                intent.putExtra(PantauConstants.URL, urlData!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else -> {
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in)
            }
        }
    }

    override fun goToLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        urlData?.let { intent.putExtra(PantauConstants.URL, it) }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun setLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }
}
