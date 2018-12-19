package com.pantaubersama.app.ui.penpol

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.fragment_pen_pol.*

/**
 * A simple [Fragment] subclass.
 *
 */
class PenPolFragment : BaseFragment<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pen_pol
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()
    }

    fun setupTabLayout() {
        val tanyaKandidatTab = TabView(context)
        tanyaKandidatTab.setTitleLabel(getString(R.string.tanya_kandidat_label))
        val kuisTab = TabView(context)
        kuisTab.setTitleLabel(getString(R.string.kuis_label))

        tab_layout.addTab(tab_layout.newTab().setCustomView(tanyaKandidatTab))
        tab_layout.addTab(tab_layout.newTab().setCustomView(kuisTab))

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
                    0 -> TanyaKandidatFragment.newInstance()
                    1 -> KuisFragment.newInstance()
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return tab_layout.tabCount
            }
        }
    }

    companion object {
        fun newInstance(): PenPolFragment {
            return PenPolFragment()
        }
    }
}
