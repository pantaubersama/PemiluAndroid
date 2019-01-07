package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_detail_janji_politik.*

class DetailJanjiPolitikActivity : CommonActivity() {

    private var id: String? = null

    companion object {
        fun setIntent(context: Context, id: String): Intent {
            val intent = Intent(context, DetailJanjiPolitikActivity::class.java)
            intent.putExtra(PantauConstants.Extra.JANPOL_ID, id)
            return intent
        }
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
        this.id = intent.getStringExtra(PantauConstants.Extra.JANPOL_ID)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        tv_title.text = "JANPOL ID $id"

        tv_content.text = getString(R.string.question_1_b_dummy) + getString(R.string.question_1_b_dummy) + getString(R.string.question_1_b_dummy) + getString(R.string.question_1_b_dummy) + getString(R.string.question_1_b_dummy)

        btn_close.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_detail_janji_politik
    }
}
