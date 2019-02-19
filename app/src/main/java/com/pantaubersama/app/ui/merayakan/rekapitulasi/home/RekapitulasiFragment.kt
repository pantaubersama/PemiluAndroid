package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

class RekapitulasiFragment : BaseFragment<RekapitulasiPresenter>() {

    @Inject
    override lateinit var presenter: RekapitulasiPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_rekapitulasi
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): RekapitulasiFragment {
            return RekapitulasiFragment()
        }

        val TAG: String = RekapitulasiFragment::class.java.simpleName
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
