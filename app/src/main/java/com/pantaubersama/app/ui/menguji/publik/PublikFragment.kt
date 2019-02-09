package com.pantaubersama.app.ui.menguji.publik

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

class PublikFragment: BaseFragment<PublikPresenter>() {

    @Inject
    override lateinit var presenter: PublikPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int = R.layout.fragment_pager_item_menguji

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}