package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.Intent
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_result.*

class KuisResultActivity : BaseActivity<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? = null

    override fun statusBarColor(): Int? = R.color.white

    override fun fetchIntentExtra() {
    }

    override fun setupUI() {
        setupToolbar(true, "", R.color.white, 0f)

        tv_kuis_result.text = spannable {
            +"Dari hasil pilhan Quiz minggu pertama,\n"
            textColor(color(R.color.black_3)) { +"Roni Handoko" }
            +" lebih suka jawaban dari Paslon no 1"
        }.toCharSequence()

        btn_see_answers.setOnClickListener {
            startActivity(Intent(this, KuisAnswerKeyActivity::class.java))
        }
    }

    override fun setLayout(): Int = R.layout.activity_kuis_result

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}