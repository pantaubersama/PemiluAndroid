package com.pantaubersama.app.ui.note

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter

class PartaiFragment : BaseFragment<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
        // view
    }

    override fun setLayout(): Int {
        return R.layout.fragment_partai
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }
}
