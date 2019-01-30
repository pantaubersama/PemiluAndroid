package com.pantaubersama.app.ui.profile.verifikasi.step3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.verifikasi.VerifikasiCameraActivity
import com.pantaubersama.app.ui.profile.verifikasi.finalstep.FinalScreenVerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step6.Step3VerifikasiPresenter
import com.pantaubersama.app.ui.profile.verifikasi.step6.Step3VerifikasiView
import com.pantaubersama.app.utils.PantauConstants.RequestCode
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.activity_step3_verifikasi.*
import java.io.File
import javax.inject.Inject

class Step3VerifikasiActivity : BaseActivity<Step3VerifikasiPresenter>(), Step3VerifikasiView {

    @Inject
    override lateinit var presenter: Step3VerifikasiPresenter

    override fun statusBarColor(): Int? = 0

    override fun fetchIntentExtra() {}

    override fun setLayout(): Int = R.layout.activity_step3_verifikasi

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "3/3"
        selfie_with_id_card_animation.enableLottie(true)
        next_button.setOnClickListener {
            VerifikasiCameraActivity.start(this, false, RequestCode.RC_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.RC_CAMERA) {
            data?.data?.let {
                presenter.submitSignaturePhoto(File(it.path))
            } ?: run {
                showFailedSubmitSignatureAlert()
            }
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    override fun onSuccessSignature() {
        val intent = Intent(this, FinalScreenVerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitSignatureAlert() {
        ToastUtil.show(this, "Gagal mengirim gambar")
    }
}
