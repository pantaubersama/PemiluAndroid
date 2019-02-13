package com.pantaubersama.app.ui.merayakan.perhitungan.create.datatps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R

class DataTPSActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_data_tps
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Data TPS", R.color.white, 4f)
    }

    companion object {
        fun start(context: Context, requsetCode: Int? = null) {
            val intent = Intent(context, DataTPSActivity::class.java)
            if (requsetCode != null) {
                (context as Activity).startActivityForResult(intent, requsetCode)
            } else {
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> {
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
