package com.pantaubersama.app.ui.wordstadium.challenge

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.wordstadium.challenge.direct.DirectChallengeActivity
import com.pantaubersama.app.ui.wordstadium.challenge.open.OpenChallengeActivity
import kotlinx.android.synthetic.main.activity_create_challenge.*

class CreateChallengeActivity : BaseActivity<CreateChallengePresenter>(), CreateChallengeView {

    override var presenter: CreateChallengePresenter = CreateChallengePresenter()

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_buat_tantangan), R.color.white, 4f)

        create_challenge_open.setOnClickListener {
            val intent = Intent(this, OpenChallengeActivity::class.java)
            startActivity(intent)
        }

        create_challenge_direct.setOnClickListener {
            val intent = Intent(this, DirectChallengeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
