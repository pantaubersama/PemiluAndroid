package com.pantaubersama.app.ui.profile.cluster.invite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.activity_undang_anggota.*
import javax.inject.Inject

class UndangAnggotaActivity : BaseActivity<UndangAnggotaPresenter>(), UndangAnggotaView {
    private lateinit var clusterUrl: String

    @Inject
    override lateinit var presenter: UndangAnggotaPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_undang_anggota), R.color.white, 4f)
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
        cluster_url.setText("http://pantaubersama.com/cluster?=$clusterUrl")
        cluster_url.setSelectAllOnFocus(true)
        undang_anggota_url_toggle.setOnCheckedChangeListener { compoundButton, selected ->
            cluster_url.isEnabled = !selected
        }
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        clusterUrl = intent.getStringExtra(PantauConstants.Cluster.CLUSTER_URL)
    }

    override fun setLayout(): Int {
        return R.layout.activity_undang_anggota
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
        lottie_loading.enableLottie(true)
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
        lottie_loading.enableLottie(false)
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
}