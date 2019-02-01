package com.pantaubersama.app.ui.profile.verifikasi.step0

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_step0_verifikasi.*
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import java.util.regex.Pattern
import javax.inject.Inject

class Step0VerifikasiActivity : BaseActivity<Step0VerifikasiPresenter>(), Step0VerifikasiView {

    @Inject
    override lateinit var presenter: Step0VerifikasiPresenter

    private val pattern = Pattern.compile(PantauConstants.Regex.KTP)
    private var isInputValid = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {}

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "0/3"

        ktp_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val matcher = pattern.matcher(query)
                isInputValid = matcher.matches()

                val icon = if (isInputValid) getDrawable(R.drawable.check_icon_green) else null
                ktp_number.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
                ktp_number_layout.error = if (!isInputValid) "Format nomor KTP tidak valid" else null
                if (!isInputValid) {
                    scroll_view.smoothScrollTo(0, ktp_number_layout.bottom)
                }
            }
        })
        ok_button.setOnClickListener {
            if (isInputValid) {
                presenter.submitKtpNumber(ktp_number.text.toString())
            } else {
                ktp_number.requestFocus()
            }
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step0_verifikasi
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun onKtpNumberSubmited() {
        val intent = Intent(this@Step0VerifikasiActivity, Step1VerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitKtpNumberAlert() {
        ToastUtil.show(this@Step0VerifikasiActivity, "Gagal mengirim data")
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