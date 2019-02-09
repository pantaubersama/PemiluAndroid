package com.pantaubersama.app.ui.penpol

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_pen_pol.*

/**
 * A simple [Fragment] subclass.
 *
 */
class PenPolFragment : CommonFragment() {
    private var selectedTabs: Int = 0

    private var tanyaKandidatFragment: TanyaKandidatFragment = TanyaKandidatFragment.newInstance()
    private var kuisFragment: KuisFragment = KuisFragment.newInstance()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        if (!isHidden) setupTabsAndFilter()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pen_pol
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
                    0 -> tanyaKandidatFragment
                    1 -> kuisFragment
                    else -> Fragment()
                }
            }

            override fun getPageTitle(position: Int): CharSequence? = when (position) {
                0 -> getString(R.string.tanya_kandidat_label)
                1 -> getString(R.string.kata_kandidat_label)
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
            0 -> {
                val intent = Intent(context, FilterTanyaKandidatActivity::class.java)
                startActivityForResult(intent, PantauConstants.TanyaKandidat.Filter.FILTER_TANYA_KANDIDAT_REQUEST_CODE)
            }
            else -> {
                val intent = Intent(context, FilterKuisActivity::class.java)
                startActivityForResult(intent, PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT)
            }
        }
    }

    companion object {
        val TAG: String = PenPolFragment::class.java.simpleName

        fun newInstance(): PenPolFragment {
            return PenPolFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tanyaKandidatFragment.onActivityResult(requestCode, resultCode, data)
        kuisFragment.onActivityResult(requestCode, resultCode, data)
    }
}