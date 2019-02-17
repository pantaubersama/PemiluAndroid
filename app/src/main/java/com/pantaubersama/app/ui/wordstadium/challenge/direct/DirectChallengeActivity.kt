package com.pantaubersama.app.ui.wordstadium.challenge.direct

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class DirectChallengeActivity : BaseActivity<DirectChallengePresenter>(), DirectChallengeView {

    override lateinit var presenter: DirectChallengePresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_direct_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_direct_challenge), R.color.white, 4f)
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
