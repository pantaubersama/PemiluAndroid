package com.pantaubersama.app.ui.profile.setting.verifikasi

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_verifikasi.*

class VerifikasiActivity : BaseActivity<VerifikasiPresenter>(), VerifikasiView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): VerifikasiPresenter? {
        return VerifikasiPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, "", R.color.white, 4f)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_verifikasi
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }

    private fun onClickAction() {
        verifikasi_ok.setOnClickListener {
            // verifikasi
        }
    }
}
