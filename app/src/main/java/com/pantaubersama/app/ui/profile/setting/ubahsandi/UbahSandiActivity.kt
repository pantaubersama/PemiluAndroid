package com.pantaubersama.app.ui.profile.setting.ubahsandi

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ubah_sandi.*

class UbahSandiActivity : BaseActivity<UbahSandiPresenter>(), UbahSandiView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): UbahSandiPresenter? {
        return UbahSandiPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_ubah_sandi), R.color.white, 4f)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_ubah_sandi
    }

    override fun showLoading() {
        // show loading
    }

    override fun dismissLoading() {
        // hide loading
    }

    private fun onClickAction() {
        ubah_sandi_ubah.setOnClickListener {
            // ubah password
        }
    }
}
