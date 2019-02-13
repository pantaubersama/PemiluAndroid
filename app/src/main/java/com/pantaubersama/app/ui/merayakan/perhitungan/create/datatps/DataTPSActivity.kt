package com.pantaubersama.app.ui.merayakan.perhitungan.create.datatps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}
