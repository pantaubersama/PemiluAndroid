package com.pantaubersama.app.ui.penpol.kuis.list

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter

/**
 * A simple [Fragment] subclass.
 *
 */
class KuisFragment : BaseFragment<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLayout(): Int {
        return R.layout.fragment_kuis
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

    companion object {
        fun newInstance(): KuisFragment {
            return KuisFragment()
        }
    }
}
