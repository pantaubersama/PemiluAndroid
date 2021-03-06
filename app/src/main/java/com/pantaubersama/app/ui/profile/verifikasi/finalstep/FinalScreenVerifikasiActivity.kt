package com.pantaubersama.app.ui.profile.verifikasi.finalstep

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.activity_final_screen_verifikasi.*

class FinalScreenVerifikasiActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        selfie_with_id_card_animation.enableLottie(true, looping = false)
        finish_button.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_final_screen_verifikasi
    }
}
