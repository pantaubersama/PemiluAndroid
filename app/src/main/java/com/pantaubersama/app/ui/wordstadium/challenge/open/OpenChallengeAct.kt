package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class OpenChallengeAct : BaseActivity<OpenChallengePresenter>(), OpenChallengeView {

    override lateinit var presenter: OpenChallengePresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.act_open_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_open_challenge), R.color.white, 4f)
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
