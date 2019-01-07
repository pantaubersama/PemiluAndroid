package com.pantaubersama.app.ui.profile.verifikasi

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.activity_final_screen_verifikasi.*

class FinalScreenVerifikasiActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        finish_button.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_final_screen_verifikasi
    }
}
