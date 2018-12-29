package com.pantaubersama.app.ui.profile.setting

import android.content.Intent
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.ui.profile.setting.ubahsandi.UbahSandiActivity
import com.pantaubersama.app.ui.profile.setting.verifikasi.VerifikasiActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity<SettingPresenter>(), SettingView {

    companion object {
        val EDIT_PROFILE = 1
        val UBAH_SANDI = 2
        val UBAH_DATA_LAPOR = 3
        val VERIFIKASI = 4
        val BADGE = 5
    }

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
            val intent = Intent(this@SettingActivity, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE)
        }
        setting_ubah_sandi.setOnClickListener {
            val intent = Intent(this@SettingActivity, UbahSandiActivity::class.java)
            startActivityForResult(intent, UBAH_SANDI)
        }
        setting_ubah_data_lapor.setOnClickListener {
            val intent = Intent(this@SettingActivity, UbahDataLaporActivity::class.java)
            startActivityForResult(intent, UBAH_DATA_LAPOR)
        }
        setting_verifikasi.setOnClickListener {
            val intent = Intent(this@SettingActivity, VerifikasiActivity::class.java)
            startActivityForResult(intent, VERIFIKASI)
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
