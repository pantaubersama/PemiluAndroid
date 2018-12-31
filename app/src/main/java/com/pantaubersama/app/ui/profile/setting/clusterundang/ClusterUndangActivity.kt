package com.pantaubersama.app.ui.profile.setting.clusterundang

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cluster_undang.*

class ClusterUndangActivity : BaseActivity<ClusterUndangPresenter>(), ClusterUndangView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): ClusterUndangPresenter? {
        return ClusterUndangPresenter()
    }

    override fun setupUI() {
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
