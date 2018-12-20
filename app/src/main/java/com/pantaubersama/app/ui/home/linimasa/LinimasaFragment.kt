package com.pantaubersama.app.ui.home.linimasa

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.home.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.home.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.fragment_linimasa.*

class LinimasaFragment : BaseFragment<BasePresenter<*>>() {
    override fun initView(view: View) {
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_linimasa
    }

    fun setupTabLayout() {
        var tabPilpres = TabView(context)
        tabPilpres.setTitleLabel(R.string.txt_tab_pilpres)
        var tabJanPol = TabView(context)
        tabJanPol.setTitleLabel(R.string.txt_tab_janji_politik)

        tab_layout.addTab(tab_layout.newTab().setCustomView(tabPilpres))
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabJanPol))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                view_pager.currentItem = tab!!.position
            }
        })
    }

    fun setupViewPager() {
        view_pager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tab_layout) {})
        view_pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> PilpresFragment.newInstance()
                    1 -> JanjiPolitikFragment()
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return tab_layout.tabCount
            }
        }
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showError(throwable: Throwable) {
    }
}
