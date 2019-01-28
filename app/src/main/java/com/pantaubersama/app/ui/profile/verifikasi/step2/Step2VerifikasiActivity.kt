package com.pantaubersama.app.ui.profile.verifikasi.step2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.verifikasi.VerificationCameraActivity
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiPresenter
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiView
import com.pantaubersama.app.ui.profile.verifikasi.step4.Step4VerifikasiActivity
import com.pantaubersama.app.utils.PantauConstants.RequestCode
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_step2_verifikasi.*
import java.io.File
import javax.inject.Inject

class Step2VerifikasiActivity : BaseActivity<Step3VerifikasiPresenter>(), Step3VerifikasiView {

    @Inject
    override lateinit var presenter: Step3VerifikasiPresenter

    override fun statusBarColor(): Int? = 0

    override fun fetchIntentExtra() {}

    override fun setLayout(): Int = R.layout.activity_step2_verifikasi

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "1/3"
        next_button.setOnClickListener {
            VerificationCameraActivity.start(this, true, RequestCode.RC_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.RC_CAMERA) {
            data?.data?.let {
                presenter.submitSelfieKtp(File(it.path))
            } ?: run {
                showFailedSubmitSelfieKtpAlert()
            }
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    override fun onSuccessSubmitSelfieKtp() {
        val intent = Intent(this, Step4VerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitSelfieKtpAlert() {
        ToastUtil.show(this, "Gagal mengunggah gambar")
    }
}
