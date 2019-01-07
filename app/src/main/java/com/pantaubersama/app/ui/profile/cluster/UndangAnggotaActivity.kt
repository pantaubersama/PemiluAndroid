package com.pantaubersama.app.ui.profile.cluster

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class UndangAnggotaActivity : BaseActivity<UndangAnggotaPresenter>(), UndangAnggotaView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): UndangAnggotaPresenter? {
        return UndangAnggotaPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_undang_anggota), R.color.white, 4f)
    }

    override fun setLayout(): Int {
        return R.layout.activity_undang_anggota
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }
}