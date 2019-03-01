package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.activity_c1_form.*

class C1FormActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_c1_form
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        when (intent.getStringExtra("c1_type")) {
            "presiden" -> {
                setupToolbar(false, "Model C1 Presiden", R.color.white, 4f)
                c1_short_hint.text = getString(R.string.c1_form_presiden_hint)
            }
            "dpr_ri" -> {
                setupToolbar(false, "Model C1-DPR RI", R.color.white, 4f)
                c1_short_hint.text = getString(R.string.c1_form_dpr_ri_hint)
            }
        }
    }
}
