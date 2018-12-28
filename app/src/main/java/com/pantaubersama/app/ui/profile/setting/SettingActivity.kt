package com.pantaubersama.app.ui.profile.setting

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity<SettingPresenter>(), SettingView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // OK
    }

    override fun initPresenter(): SettingPresenter? {
        return SettingPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_setting), R.color.white, 4f)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_setting
    }

    override fun showLoading() {
        // Show Loading
    }

    override fun dismissLoading() {
        // Hide Loading
    }

    fun onClickAction() {
        setting_ubah_profil.setOnClickListener {
            // ubah profil
        }
        setting_ubah_sandi.setOnClickListener {
            // ubah sandi
        }
        setting_ubah_data_lapor.setOnClickListener {
            // ubah data lapor
        }
        setting_verifikasi.setOnClickListener {
            // verifikasi
        }
        setting_badge.setOnClickListener {
            // badge
        }
        setting_connect_twitter.setOnClickListener {
            // connect with twitter
        }
        setting_pusat_bantuan.setOnClickListener {
            // pusat bantuan
        }
        setting_pedoman_komunitas.setOnClickListener {
            // pedoman komunitas
        }
        setting_tentang.setOnClickListener {
            // tentang pantau bersama
        }
        setting_berikan_nilai.setOnClickListener {
            // berikan nikai kami
        }
        setting_bagikan_aplikasi.setOnClickListener {
            // bagikan aplikasi pantau bersama
        }
        setting_logout.setOnClickListener {
            // logout aplikasi
        }
    }
}
