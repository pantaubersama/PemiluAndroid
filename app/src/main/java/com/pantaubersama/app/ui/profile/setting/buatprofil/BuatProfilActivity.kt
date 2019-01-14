package com.pantaubersama.app.ui.profile.setting.buatprofil

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_buat_profil.*

class BuatProfilActivity : BaseActivity<BuatProfilPresenter>(), BuatProfilView {

    override var presenter: BuatProfilPresenter
        get() = BuatProfilPresenter()
        set(value) {}

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_buat_profil
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_buat_profilmu), R.color.white, 4f)
        onClickAction()
    }

    private fun onClickAction() {
        buat_profil_submit.setOnClickListener {
            // lanjut
        }
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }
}
