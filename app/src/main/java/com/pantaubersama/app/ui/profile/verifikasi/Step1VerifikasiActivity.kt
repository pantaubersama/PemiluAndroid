package com.pantaubersama.app.ui.profile.verifikasi

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.activity_step1_verifikasi.*

class Step1VerifikasiActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        verification_step_indicator.text = "0/3"
        ok_button.setOnClickListener {
            val intent = Intent(this@Step1VerifikasiActivity, Step2VerifikasiActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step1_verifikasi
    }
}
