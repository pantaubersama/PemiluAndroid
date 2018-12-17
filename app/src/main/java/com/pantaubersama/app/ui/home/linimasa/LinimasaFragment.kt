package com.pantaubersama.app.ui.home.linimasa

import android.support.design.widget.TabLayout
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import kotlinx.android.synthetic.main.fragment_linimasa.*

class LinimasaFragment : BaseFragment<BasePresenter<*>>() {
    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
        setupTabLayout()
        setupViewPager()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_linimasa
    }

    fun setupTabLayout() {
//        val tabPilpres = TabLayout.Tab()
//        tabPilpres.contentDescription = "OY 1"
//        tabPilpres.text = getString(R.string.txt_tab_pilpres)
//        val tabJanjiPolitik = TabLayout.Tab()
//        tabJanjiPolitik.contentDescription = "OY 2"
//        tabJanjiPolitik.text = getString(R.string.txt_tab_janji_politik)
//        tab_layout.addTab(tabPilpres)
//        tab_layout.addTab(tabJanjiPolitik)
    }

    fun setupViewPager() {

    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
