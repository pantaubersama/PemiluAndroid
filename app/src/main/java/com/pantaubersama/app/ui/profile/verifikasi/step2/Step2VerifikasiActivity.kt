package com.pantaubersama.app.ui.profile.verifikasi.step2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.verifikasi.VerifikasiCameraActivity
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiActivity
import com.pantaubersama.app.utils.PantauConstants.RequestCode
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_step2_verifikasi.*
import java.io.File
import javax.inject.Inject

class Step2VerifikasiActivity : BaseActivity<Step2VerifikasiPresenter>(), Step2VerifikasiView {

    @Inject
    override lateinit var presenter: Step2VerifikasiPresenter

    override fun statusBarColor(): Int? = 0

    override fun fetchIntentExtra() {}

    override fun setLayout(): Int = R.layout.activity_step2_verifikasi

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "2/3"
        next_button.setOnClickListener {
            VerifikasiCameraActivity.start(this, false, RequestCode.RC_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.RC_CAMERA) {
            data?.data?.let {
                presenter.submitKtpPhoto(File(it.path))
            } ?: run {
                showFailedSubmitKtpPhotoAlert()
            }
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    override fun onSuccessSubmitKtpPhoto() {
        val intent = Intent(this, Step3VerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitKtpPhotoAlert() {
        ToastUtil.show(this, "Gagal mengunggah gambar")
    }
}
