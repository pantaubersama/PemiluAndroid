package com.pantaubersama.app.ui.penpol

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.widget.TabView
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_pen_pol.*
/**
 * A simple [Fragment] subclass.
 *
 */
class PenPolFragment : CommonFragment() {
    private var selectedTabs: Int = 0

    private var tanyaKandidatFragment: TanyaKandidatFragment? = TanyaKandidatFragment.newInstance()
    private var kuisFragment: KuisFragment? = KuisFragment.newInstance()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()
        btn_filter?.setOnClickListener {
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
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pen_pol
    }

    fun setupTabLayout() {
        val tanyaKandidatTab = TabView(context)
        tanyaKandidatTab.setTitleLabel(getString(R.string.tanya_kandidat_label))
        val kuisTab = TabView(context)
        kuisTab.setTitleLabel(getString(R.string.kuis_label))

        tab_layout?.addTab(tab_layout?.newTab()?.setCustomView(tanyaKandidatTab)!!)
        tab_layout?.addTab(tab_layout?.newTab()?.setCustomView(kuisTab)!!)

        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTabs = tab!!.position
                view_pager?.currentItem = tab.position
            }
        })
    }

    fun setupViewPager() {
        view_pager?.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tab_layout) {})
        view_pager?.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> tanyaKandidatFragment!!
                    1 -> kuisFragment!!
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return tab_layout?.tabCount!!
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
        if (tanyaKandidatFragment != null) {
            tanyaKandidatFragment?.onActivityResult(requestCode, resultCode, data)
        }
        if (kuisFragment != null) {
            kuisFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }
}