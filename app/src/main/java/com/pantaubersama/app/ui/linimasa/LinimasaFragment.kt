package com.pantaubersama.app.ui.linimasa

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.ui.widget.TabView
import com.pantaubersama.app.utils.PantauConstants.RequestCode.FILTER_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.FILTER_PILPRES
import kotlinx.android.synthetic.main.fragment_linimasa.*

class LinimasaFragment : BaseFragment<BasePresenter<*>>() {
    private var selectedTabs: Int = 0

    private var pilpresFragment = PilpresFragment.newInstance()
    private var janjiPolitikFragment = JanjiPolitikFragment()

    override fun initView(view: View) {
        setupTabLayout()
        setupViewPager()
        btn_filter.setOnClickListener {
            when (selectedTabs) {
                0 -> startActivityForResult(Intent(
                    context, FilterPilpresActivity::class.java),
                    FILTER_PILPRES)
                else -> startActivityForResult(Intent(
                    context, FilterJanjiPolitikActivity::class.java),
                    FILTER_JANPOL)
            }
        }
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun setLayout(): Int {
        return R.layout.fragment_linimasa
    }

    fun setupTabLayout() {
        val tabPilpres = TabView(context)
        tabPilpres.setTitleLabel(R.string.txt_tab_linimasa)
        val tabJanPol = TabView(context)
        tabJanPol.setTitleLabel(R.string.txt_tab_janji_politik)

        tab_layout.addTab(tab_layout.newTab().setCustomView(tabPilpres))
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabJanPol))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                pilpresFragment.scrollToTop(true)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTabs = tab!!.position
                view_pager.currentItem = tab.position
            }
        })
    }

    fun setupViewPager() {
        view_pager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tab_layout) {})
        view_pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> pilpresFragment
                    1 -> janjiPolitikFragment
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return tab_layout.tabCount
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    FILTER_PILPRES -> {
                        pilpresFragment.getFeedsData()
                    }
                    FILTER_JANPOL -> {
                        janjiPolitikFragment.getJanjiPolitikList()
                    }
                }
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
