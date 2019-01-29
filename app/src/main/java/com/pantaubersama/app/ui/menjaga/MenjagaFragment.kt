package com.pantaubersama.app.ui.menjaga

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.menjaga.lapor.LaporFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.fragment_menjaga.*

class MenjagaFragment : CommonFragment() {
    private val laporFragment: LaporFragment = LaporFragment.newInstance()
    private var selectedTabs: Int = 0

    companion object {
        val TAG = MenjagaFragment::class.java.simpleName

        fun newInstance(): MenjagaFragment {
            return MenjagaFragment()
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_menjaga
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()
    }

    private fun setupTabLayout() {
        val laporTab = TabView(context)
        laporTab.setTitleLabel(R.string.lapor_label)

        tab_layout.addTab(tab_layout.newTab().setCustomView(laporTab))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val currentFragment = childFragmentManager.findFragmentByTag(
                    "android:switcher:" + R.id.view_pager + ":" + view_pager.currentItem
                ) as CommonFragment?

                currentFragment?.scrollToTop()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTabs = tab!!.position
                view_pager.currentItem = tab.position
            }
        })
    }

    private fun setupViewPager() {
        view_pager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tab_layout) {})
        view_pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): androidx.fragment.app.Fragment {
                return when (position) {
                    0 -> laporFragment
                    else -> androidx.fragment.app.Fragment()
                }
            }

            override fun getCount(): Int {
                return tab_layout.tabCount
            }
        }
    }
}
