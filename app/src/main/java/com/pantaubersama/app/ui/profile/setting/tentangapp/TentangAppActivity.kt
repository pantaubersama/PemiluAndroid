package com.pantaubersama.app.ui.profile.setting.tentangapp

import android.annotation.SuppressLint
import android.os.Bundle
import com.pantaubersama.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_tentang_app.*
import javax.inject.Inject

class TentangAppActivity : BaseActivity<TentangAppPresenter>(), TentangAppView {

    @Inject
    override lateinit var presenter: TentangAppPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupUI(savedInstanceState: Bundle?) {
        tentang_app_versi.text = "Versi " + BuildConfig.VERSION_NAME
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_tentang_app
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dsimiss
    }

    private fun onClickAction() {
        tentang_app_close.setOnClickListener {
            finish()
        }
        license_text.setOnClickListener {
            ChromeTabUtil(this@TentangAppActivity).forceLoadUrl(PantauConstants.Profile.URL_TENTANG_PANTAU_BERSAMA)
        }
    }
}
