package com.pantaubersama.app.ui.wordstadium.challenge

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class CreateChallengeActivity : BaseActivity<CreateChallengePresenter>(), CreateChallengeView {

    override lateinit var presenter: CreateChallengePresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_buat_tantangan), R.color.white, 4f)
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
