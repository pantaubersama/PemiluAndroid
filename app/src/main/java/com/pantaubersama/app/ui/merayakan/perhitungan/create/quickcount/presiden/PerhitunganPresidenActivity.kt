package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.activity_perhitungan_presiden.*

class PerhitunganPresidenActivity : CommonActivity() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_presiden
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Presiden", R.color.white, 4f)
        paslon_1_inc_button.setOnClickListener {
            val count = paslon_1_count_field.text.toString().toInt()
            paslon_1_count_field.setText(count.plus(1).toString())
        }
        paslon_2_inc_button.setOnClickListener {
            val count = paslon_2_count_field.text.toString().toInt()
            paslon_2_count_field.setText(count.plus(1).toString())
        }
        golput_inc_button.setOnClickListener {
            val count = golput_count_field.text.toString().toInt()
            golput_count_field.setText(count.plus(1).toString())
        }
        save_button.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
