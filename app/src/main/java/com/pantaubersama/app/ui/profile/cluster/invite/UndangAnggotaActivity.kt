package com.pantaubersama.app.ui.profile.cluster.invite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CompoundButton
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PantauConstants.Cluster.CLUSTER_ID
import com.pantaubersama.app.utils.PantauConstants.Cluster.CLUSTER_URL
import com.pantaubersama.app.utils.PantauConstants.Cluster.INVITE_LINK_ACTIVE
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IS_MODERATOR
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_undang_anggota.*
import javax.inject.Inject

class UndangAnggotaActivity : BaseActivity<UndangAnggotaPresenter>(), UndangAnggotaView {
    private lateinit var clusterUrl: String
    private lateinit var clusterId: String
    private var isUrlActive = false
    private var isModerator = false

    private var switchListener: CompoundButton.OnCheckedChangeListener? = null

    @Inject
    override lateinit var presenter: UndangAnggotaPresenter

    override fun statusBarColor(): Int? = 0
    override fun setLayout(): Int = R.layout.activity_undang_anggota

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        fun setIntent(context: Context, clusterUrl: String?, clusterId: String?, isUrlActive: Boolean?, isModerator: Boolean): Intent {
            val intent = Intent(context, UndangAnggotaActivity::class.java)
            intent.putExtra(CLUSTER_URL, clusterUrl)
            intent.putExtra(CLUSTER_ID, clusterId)
            intent.putExtra(INVITE_LINK_ACTIVE, isUrlActive)
            intent.putExtra(EXTRA_IS_MODERATOR, isModerator)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        clusterUrl = intent.getStringExtra(CLUSTER_URL)
        clusterId = intent.getStringExtra(CLUSTER_ID)
        isUrlActive = intent.getBooleanExtra(INVITE_LINK_ACTIVE, isUrlActive)
        isModerator = intent.getBooleanExtra(EXTRA_IS_MODERATOR, isModerator)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_undang_anggota), R.color.white, 4f)
        fl_invite_email.visibleIf(isModerator)
        tv_magic_lick_desc.text = getText(if (isModerator) R.string.deskripsi_undang_lewat_tautan_moderator else R.string.deskripsi_undang_lewat_tautan_non_moderator)
        setupMagicLink()
        undang_anggota_btn_undang.setOnClickListener {
            if (undang_anggota_text_email.text.isNotEmpty()) {
                val pattern = Patterns.EMAIL_ADDRESS
                val matcher = pattern.matcher(undang_anggota_text_email.text)
                if (matcher.matches()) {
                    presenter.invite(undang_anggota_text_email.text.toString())
                } else {
                    undang_anggota_text_email.error = "Email tidak valid"
                }
            } else {
                undang_anggota_text_email.error = "Tidak boleh kosong"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupMagicLink() {
        undang_anggota_url_toggle.isChecked = isUrlActive
        cluster_url.text = "%scluster?=%s".format(BuildConfig.PANTAU_WEB_URL, clusterUrl)
        if (isUrlActive) {
            cluster_url.background = getDrawable(R.drawable.bg_rounded_outline_white)
            if (!isModerator) tv_link_status.text = getString(R.string.label_link_undangan_aktif)
        } else {
            cluster_url.isEnabled = false
            cluster_url.background = getDrawable(R.drawable.bg_rounded_gray_3)
            if (!isModerator) tv_link_status.text = getString(R.string.label_link_undangan_non_aktif)
        }
        if (isModerator) {
            switchListener = CompoundButton.OnCheckedChangeListener { compoundButton, enable ->
                cluster_url.isEnabled = enable
                presenter.enableDisableUrlInvite(clusterId, enable)
                setResult(Activity.RESULT_OK)
            }
            undang_anggota_url_toggle.setOnCheckedChangeListener(switchListener)
        } else {
            undang_anggota_url_toggle.isClickable = false
        }

        cluster_url.setOnClickListener {
            CopyUtil.copyToClipBoard(this@UndangAnggotaActivity, cluster_url.text.toString(), "Tautan berhasil disalin") }
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
        lottie_loading.enableLottie(true, lottie_loading)
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun showSuccessInviteAlert() {
        ToastUtil.show(this@UndangAnggotaActivity, "Berhasil mengirim undangan ke pengguna")
    }

    override fun showFailedInviteAlert() {
        ToastUtil.show(this@UndangAnggotaActivity, "Gagal mengirim undangan ke pengguna")
    }

    override fun disableView() {
        invite_by_email_container.enable(false)
    }

    override fun enableView() {
        invite_by_email_container.enable(true)
    }

    override fun removeEmail() {
        undang_anggota_text_email.setText("")
    }

    override fun showSuccessDisableUrlInviteAlert() {
        ToastUtil.show(this@UndangAnggotaActivity, "Bagikan undangan via link dinon-aktifkan")
        cluster_url.clearFocus()
        cluster_url.background = getDrawable(R.drawable.bg_rounded_gray_3)
    }

    override fun showSuccessEnableUrlInviteAlert() {
        CopyUtil.copyToClipBoard(this@UndangAnggotaActivity, cluster_url.text.toString(), "Tautan berhasil disalin")
        cluster_url.background = getDrawable(R.drawable.bg_rounded_outline_white)
    }

    override fun showFailedDisableUrlInviteAlert() {
        ToastUtil.show(this@UndangAnggotaActivity, "Gagal menon-aktifkan undangan via link")
    }

    override fun showFailedEnableUrlInviteAlert() {
        ToastUtil.show(this@UndangAnggotaActivity, "Gagal mengaktifkan undangan via link")
    }

    override fun reverseView(enable: Boolean) {
        undang_anggota_url_toggle.setOnCheckedChangeListener(null)
        undang_anggota_url_toggle.isChecked = enable
        undang_anggota_url_toggle.setOnCheckedChangeListener(switchListener)
    }
}