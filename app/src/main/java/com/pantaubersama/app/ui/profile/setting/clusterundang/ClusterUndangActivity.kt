package com.pantaubersama.app.ui.profile.setting.clusterundang

import android.os.Bundle
import android.util.Patterns
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_cluster_undang.*
import javax.inject.Inject

class ClusterUndangActivity : BaseActivity<ClusterUndangPresenter>(), ClusterUndangView {
    private lateinit var clusterId: String

    @Inject
    override lateinit var presenter: ClusterUndangPresenter

    override fun statusBarColor(): Int? = 0
    override fun setLayout(): Int = R.layout.activity_cluster_undang

    override fun fetchIntentExtra() {
        clusterId = intent.getStringExtra(PantauConstants.Cluster.CLUSTER_ID)
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_undang_anggota), R.color.white, 4f)
        onClickAction()
    }

    override fun showLoading() {
        showProgressDialog("Mohon tunggu ...")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    private fun onClickAction() {
        cluster_undang_submit.setOnClickListener {
            if (cluster_undang_email.text.isNotEmpty()) {
                val pattern = Patterns.EMAIL_ADDRESS
                val matcher = pattern.matcher(cluster_undang_email.text)
                if (matcher.matches()) {
                    presenter.invite(cluster_undang_email.text.toString(), clusterId)
                } else {
                    cluster_undang_email.error = "Email tidak valid"
                }
            } else {
                cluster_undang_email.error = "Tidak boleh kosong"
            }
        }
    }

    override fun showSuccessInviteAlert() {
        ToastUtil.show(this@ClusterUndangActivity, "Berhasil mengirim undangan ke pengguna")
    }

    override fun showFailedInviteAlert() {
        ToastUtil.show(this@ClusterUndangActivity, "Gagal mengirim undangan ke pengguna")
    }

    override fun finishThisSection() {
        finish()
    }
}
