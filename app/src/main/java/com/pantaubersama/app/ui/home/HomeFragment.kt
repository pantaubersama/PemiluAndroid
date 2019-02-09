package com.pantaubersama.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*

abstract class HomeFragment : CommonFragment() {

    private var currentTabPosition: Int = 0

    abstract val pagerFragments: List<Pair<Fragment, String>>

    override fun setLayout(): Int = R.layout.fragment_view_pager

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        if (!isHidden) setupTabsAndFilter()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) setupTabsAndFilter()
    }

    private fun setupViewPager() {
        view_pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = pagerFragments[position].first
            override fun getPageTitle(position: Int): CharSequence? = pagerFragments[position].second
            override fun getCount(): Int = pagerFragments.size
        }

        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentTabPosition = position
            }
        })
    }

    private fun setupTabsAndFilter() {
        (requireActivity() as HomeActivity).setupTabsAndFilter(view_pager) {
            onFilterClicked(currentTabPosition)
        }
    }

    abstract fun onFilterClicked(tabPosition: Int)

    fun scrollToTop() {
        val pagerFragment = childFragmentManager.findFragmentByTag("android:switcher:"
            + R.id.view_pager + ":" + currentTabPosition) as CommonFragment?
        pagerFragment?.scrollToTop()
    }
}