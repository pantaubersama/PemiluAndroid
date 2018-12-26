package com.pantaubersama.app.ui.profile.verifikasi

import android.content.Intent
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import kotlinx.android.synthetic.main.activity_step2_verifikasi.*

class Step2VerifikasiActivity : BaseActivity<BasePresenter<*>>() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun setupUI() {
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

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
