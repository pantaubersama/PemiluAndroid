package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R

class PerhitunganDPRRIActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_dprri
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "DPR RI", R.color.white, 4f)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
