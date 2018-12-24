package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import kotlinx.android.synthetic.main.activity_kuis.*

class KuisActivity : BaseActivity<BasePresenter<*>>() {
    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        back_icon.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_kuis
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}