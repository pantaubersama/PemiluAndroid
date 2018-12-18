package com.pantaubersama.app.ui.home.linimasa.pilpres

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter

class PilpresFragment : BaseFragment<BasePresenter<*>>() {

    companion object {
//        TODO : val ARGS1 = "ARGS1"
//
//        fun newInstance(var1: String): PilpresFragment {
//            val fragment = PilpresFragment()
//            var args = Bundle()
//            args.putString(ARGS1, var1)
//            fragment.arguments = args
//            return fragment
//        }
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}