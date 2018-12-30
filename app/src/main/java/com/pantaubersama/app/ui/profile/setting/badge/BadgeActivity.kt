package com.pantaubersama.app.ui.profile.setting.badge

import android.app.Activity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_badge.*

class BadgeActivity : BaseActivity<BadgePresenter>(), BadgeView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): BadgePresenter? {
        return BadgePresenter()
    }

    override fun setupUI() {
        onClickAction()
    }

    private fun onClickAction() {
        badge_close.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_badge
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }
}
