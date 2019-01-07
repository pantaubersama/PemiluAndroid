package com.pantaubersama.app.ui.profile.setting.tentangapp

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
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

    override fun setupUI(savedInstanceState: Bundle?) {
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
    }
}
