package com.pantaubersama.app.ui.profile.cluster

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class RequestClusterActivity : BaseActivity<RequestClusterPresenter>() {

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_request_cluster

    override fun fetchIntentExtra() {
    }

    override fun initPresenter(): RequestClusterPresenter? {
        return RequestClusterPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, "Request Buat Cluster", R.color.white, 4f)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}
