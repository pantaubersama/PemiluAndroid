package com.pantaubersama.app.ui.profile.connect

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class ConnectActivity : BaseActivity<ConnectPresenter>(), ConnectView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {

    }

    override fun initPresenter(): ConnectPresenter? {
        return ConnectPresenter()
    }

    override fun setupUI() {
        setupToolbar(false, getString(R.string.title_connet), R.color.white, 4f)
    }

    override fun setLayout(): Int {
        return R.layout.activity_connect
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismis
    }
}
