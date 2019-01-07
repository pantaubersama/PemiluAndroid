package com.pantaubersama.app.ui.profile.verifikasi.step2

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiActivity
import kotlinx.android.synthetic.main.activity_step2_verifikasi.*

class Step2VerifikasiActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "1/3"
        next_button.setOnClickListener {
            val intent = Intent(this@Step2VerifikasiActivity, Step3VerifikasiActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step2_verifikasi
    }
}
