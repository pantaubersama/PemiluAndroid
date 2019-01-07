package com.pantaubersama.app.ui.profile.verifikasi.step1

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_step1_verifikasi.*
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.verifikasi.step2.Step2VerifikasiActivity
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import java.util.regex.Pattern
import javax.inject.Inject

class Step1VerifikasiActivity : BaseActivity<Step1VerifikasiPresenter>(), Step1VerifikasiView {

    @Inject
    override lateinit var presenter: Step1VerifikasiPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "0/3"
        ok_button.setOnClickListener {
            val pattern = Pattern.compile(PantauConstants.Regex.KTP)
            val matcher = pattern.matcher(ktp_number.text)
            if (matcher.matches()) {
                presenter?.submitKtpNumber(ktp_number.text.toString())
            } else {
                ktp_number.error = "Format nomor KTP tidak valid"
            }
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step1_verifikasi
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun onKtpNumberSubmited() {
        val intent = Intent(this@Step1VerifikasiActivity, Step2VerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitKtpNumberAlert() {
        ToastUtil.show(this@Step1VerifikasiActivity, "Gagal mengirim data")
    }

    override fun disableView() {
        ok_button.enable(false)
        container.enable(false)
    }

    override fun enableView() {
        ok_button.enable(true)
        container.enable(true)
    }
}