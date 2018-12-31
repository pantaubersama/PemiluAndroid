package com.pantaubersama.app.ui.profile.setting.tentangapp

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_tentang_app.*

class TentangAppActivity : BaseActivity<TentangAppPresenter>(), TentangAppView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): TentangAppPresenter? {
        return TentangAppPresenter()
    }

    override fun setupUI() {
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
