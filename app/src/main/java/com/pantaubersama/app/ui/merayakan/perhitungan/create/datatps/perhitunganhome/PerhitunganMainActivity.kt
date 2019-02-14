package com.pantaubersama.app.ui.merayakan.perhitungan.create.datatps.perhitunganhome

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R

class PerhitunganMainActivity : CommonActivity() {
    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitunganmain
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Perhitungan", R.color.white, 4f)
    }
}
