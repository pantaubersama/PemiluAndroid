package com.pantaubersama.app.ui.linimasa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OPEN_TAB_TYPE
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_PILPRES
import kotlinx.android.synthetic.main.fragment_linimasa.*

class LinimasaFragment : CommonFragment() {
    private var selectedTabs: Int = 0

    private var pilpresFragment = PilpresFragment.newInstance()
    private var janjiPolitikFragment = JanjiPolitikFragment()

    private var selectedTab = 0

    companion object {
        val TAG: String = LinimasaFragment::class.java.simpleName

        fun newInstanceOpenJanpol(): LinimasaFragment {
            val fragment = LinimasaFragment()
            val args = Bundle()
            args.putInt(EXTRA_OPEN_TAB_TYPE, EXTRA_TYPE_JANPOL)
            fragment.arguments = args
            return fragment
        }
    }

    override fun fetchArguments(args: Bundle?) {
        args?.apply {
            selectedTab = getInt(EXTRA_OPEN_TAB_TYPE, 0)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        view_pager.currentItem = selectedTab
        if (!isHidden) setupTabsAndFilter()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_linimasa
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) setupTabsAndFilter()
    }

    private fun setupViewPager() {
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                selectedTabs = position
            }
        })
        view_pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> pilpresFragment
                    1 -> janjiPolitikFragment
                    else -> Fragment()
                }
            }

            override fun getPageTitle(position: Int): CharSequence? = when(position) {
                0 -> getString(R.string.txt_tab_linimasa)
                1 -> getString(R.string.txt_tab_janji_politik)
                else -> null
            }

            override fun getCount(): Int = 2
        }
    }

    private fun setupTabsAndFilter() {
        (requireActivity() as HomeActivity).setupTabsAndFilter(view_pager, ::onFilterClicked)
    }

    private fun onFilterClicked() {
        when (selectedTabs) {
            0 -> startActivityForResult(Intent(
                context, FilterPilpresActivity::class.java),
                RC_FILTER_PILPRES)
            else -> startActivityForResult(Intent(
                context, FilterJanjiPolitikActivity::class.java),
                RC_FILTER_JANPOL)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    RC_FILTER_PILPRES -> {
                        pilpresFragment.getFeedsData()
                    }
                    RC_FILTER_JANPOL -> {
                        janjiPolitikFragment.getJanjiPolitikList()
                    }
                }
            }
        }
    }
}
