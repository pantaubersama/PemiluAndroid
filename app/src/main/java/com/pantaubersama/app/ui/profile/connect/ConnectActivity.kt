package com.pantaubersama.app.ui.profile.connect

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

class ConnectActivity : BaseActivity<ConnectPresenter>(), ConnectView {

    @Inject
    override lateinit var presenter: ConnectPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
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
