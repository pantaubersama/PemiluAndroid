package com.pantaubersama.app.ui.tanyakandidat.create

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_create_tanya_kandidat.*

class CreateTanyaKandidatActivity : BaseActivity<CreateTanyaKandidatPresenter>() {

    override fun statusBarColor(): Int {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.create_question), R.color.white, 4f)
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_tanya_kandidat
    }

    override fun initPresenter(): CreateTanyaKandidatPresenter? {
        return CreateTanyaKandidatPresenter()
    }

    override fun showLoading() {
        page_loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        page_loading.visibility = View.GONE
    }
}
