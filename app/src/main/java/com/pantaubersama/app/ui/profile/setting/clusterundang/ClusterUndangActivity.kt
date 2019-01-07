package com.pantaubersama.app.ui.profile.setting.clusterundang

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_cluster_undang.*

class ClusterUndangActivity : BaseActivity<ClusterUndangPresenter>(), ClusterUndangView {

    override var presenter: ClusterUndangPresenter = ClusterUndangPresenter()

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {

    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_undang_anggota), R.color.white, 4f)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_cluster_undang
    }

    override fun showLoading() {
        // Show
    }

    override fun dismissLoading() {
        // Dismiss
    }

    private fun onClickAction() {
        cluster_undang_submit.setOnClickListener {
            // undang anggota
        }
    }
}
