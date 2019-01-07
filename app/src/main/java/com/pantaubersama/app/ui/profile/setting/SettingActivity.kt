package com.pantaubersama.app.ui.profile.setting

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.setting.clusterundang.ClusterUndangActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.panduankomunitas.PanduanKomunitasActivity
import com.pantaubersama.app.ui.profile.setting.tentangapp.TentangAppActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.ui.profile.setting.ubahsandi.UbahSandiActivity
import com.pantaubersama.app.ui.profile.verifikasi.Step1VerifikasiActivity
import kotlinx.android.synthetic.main.activity_setting.*
import javax.inject.Inject

class SettingActivity : BaseActivity<SettingPresenter>(), SettingView {
    @Inject
    lateinit var interactor: LoginInteractor

    companion object {
        val EDIT_PROFILE = 1
        val UBAH_SANDI = 2
        val UBAH_DATA_LAPOR = 3
        val VERIFIKASI = 4
        val BADGE = 5
        val CLUSTER_UNDANG = 6
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // OK
    }

    override fun initPresenter(): SettingPresenter? {
        return SettingPresenter(interactor)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
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
            val intent = Intent(this@SettingActivity, Step1VerifikasiActivity::class.java)
            startActivityForResult(intent, VERIFIKASI)
        }
        setting_badge.setOnClickListener {
            val intent = Intent(this@SettingActivity, BadgeActivity::class.java)
            startActivityForResult(intent, BADGE)
        }
        setting_cluster_undang.setOnClickListener {
            val intent = Intent(this@SettingActivity, ClusterUndangActivity::class.java)
            startActivityForResult(intent, CLUSTER_UNDANG)
        }
        setting_connect_twitter.setOnClickListener {
            // connect with twitter
        }
        setting_pusat_bantuan.setOnClickListener {
            // pusat bantuan
        }
        setting_pedoman_komunitas.setOnClickListener {
            val intent = Intent(this@SettingActivity, PanduanKomunitasActivity::class.java)
            startActivity(intent)
        }
        setting_tentang.setOnClickListener {
            val intent = Intent(this@SettingActivity, TentangAppActivity::class.java)
            startActivity(intent)
        }
        setting_berikan_nilai.setOnClickListener {
            // berikan nikai kami
        }
        setting_bagikan_aplikasi.setOnClickListener {
            // bagikan aplikasi pantau bersama
        }
        setting_logout.setOnClickListener {
            presenter?.logOut(BuildConfig.PANTAU_CLIENT_ID, BuildConfig.PANTAU_CLIENT_SECRET, interactor.getToken())
        }
    }

    override fun goToLogin() {
        val intent = Intent(this@SettingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
