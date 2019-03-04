package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_c1_form.*

class C1FormActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_c1_form
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var title: String = ""
        when (intent.getStringExtra(PantauConstants.Merayakan.C1_MODEL_TYPE)) {
            "presiden" -> {
                title = "Model C1 Presiden"
                c1_short_hint.text = getString(R.string.c1_form_presiden_hint)
            }
            "dpr_ri" -> {
                title = "Model C1-DPR RI"
                c1_short_hint.text = getString(R.string.c1_form_dpr_ri_hint)
            }
            "dpd" -> {
                title = "Model C1-DPD RI"
                c1_short_hint.text = getString(R.string.c1_form_dpd_ri_hint)
            }
            "dprd_provinsi" -> {
                title = "Model C1-DPRD Provinsi"
                c1_short_hint.text = getString(R.string.c1_form_dprd_provinsi_hint)
            }
            "dprd_kabupaten" -> {
                title = "Model C1-DPRD Kabupaten"
                c1_short_hint.text = getString(R.string.c1_form_dprd_kabupaten_hint)
            }
        }

        setupToolbar(false, title, R.color.white, 4f)
    }
}
