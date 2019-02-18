package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd.PerhitunganDPDActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdprovinsi.PerhitunganDPRDProvinsiActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri.PerhitunganDPRRIActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import kotlinx.android.synthetic.main.activity_perhitunganmain.*

class PerhitunganMainActivity : CommonActivity() {
    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitunganmain
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Perhitungan", R.color.white, 4f)
        presiden_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganPresidenActivity::class.java))
        }
        dpr_ri_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRRIActivity::class.java))
        }
        dpd_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPDActivity::class.java))
        }
        dpr_provinsi_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDProvinsiActivity::class.java))
        }
    }
}
