package com.pantaubersama.app.ui.profile.setting

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.setting.clusterundang.ClusterUndangActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.panduankomunitas.PanduanKomunitasActivity
import com.pantaubersama.app.ui.profile.setting.tentangapp.TentangAppActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.ui.profile.setting.ubahsandi.UbahSandiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.verified_layout.*
import javax.inject.Inject

class SettingActivity : BaseActivity<SettingPresenter>(), SettingView {

    @Inject
    override lateinit var presenter: SettingPresenter
    private var verifiedDialog: Dialog? = null

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

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_setting), R.color.white, 4f)
        onClickAction()
        presenter.getProfile()
    }

    override fun onSuccessGetProfile(profile: Profile) {
        setting_verifikasi.setOnClickListener {
            if (profile.verified) {
                openVerifiedDialog(profile)
            } else {
                val intent = Intent(this@SettingActivity, Step1VerifikasiActivity::class.java)
                startActivityForResult(intent, VERIFIKASI)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openVerifiedDialog(profile: Profile) {
        verifiedDialog = Dialog(this@SettingActivity)
        verifiedDialog?.setContentView(R.layout.verified_layout)
        verifiedDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = verifiedDialog?.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        verifiedDialog?.iv_user_avatar?.loadUrl(profile.avatar.url, R.drawable.ic_avatar_placeholder)
        verifiedDialog?.tv_user_name?.text = profile.name
        verifiedDialog?.user_username?.text = "@" + profile.username
        if (profile.verified) setVerified() else setUnverified()
        verifiedDialog?.show()
    }

    private fun setUnverified() {
        verifiedDialog?.verified_icon?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@SettingActivity, R.color.gray_dark_1), PorterDuff.Mode.MULTIPLY)
        verifiedDialog?.verified_text?.text = getString(R.string.txt_belum_verifikasi)
        verifiedDialog?.verified_text?.setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.gray_dark_1))
        verifiedDialog?.verified_button?.setBackgroundResource(R.drawable.rounded_outline_gray)
    }

    private fun setVerified() {
        verifiedDialog?.verified_icon?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@SettingActivity, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        verifiedDialog?.verified_text?.text = getString(R.string.txt_terverifikasi)
        verifiedDialog?.verified_text?.setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.colorAccent))
        verifiedDialog?.verified_button?.setBackgroundResource(R.drawable.rounded_outline_green)
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
            presenter.logOut(BuildConfig.PANTAU_CLIENT_ID, BuildConfig.PANTAU_CLIENT_SECRET)
        }
    }

    override fun goToLogin() {
        val intent = Intent(this@SettingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
