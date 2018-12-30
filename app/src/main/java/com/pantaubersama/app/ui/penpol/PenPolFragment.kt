package com.pantaubersama.app.ui.penpol

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.widget.TabView
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_pen_pol.*
/**
 * A simple [Fragment] subclass.
 *
 */
class PenPolFragment : BaseFragment<BasePresenter<*>>() {
    private var selectedTabs: Int = 0

    private var tanyaKandidatFragment: TanyaKandidatFragment? = TanyaKandidatFragment.newInstance()
    private var kuisFragment: KuisFragment? = KuisFragment.newInstance()

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
        setupTabLayout()
        setupViewPager()
        btn_create.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivityForResult(intent, PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE)
        }
        btn_filter?.setOnClickListener {
            when (selectedTabs) {
                0 -> {
                    val intent = Intent(context, FilterTanyaKandidatActivity::class.java)
                    startActivityForResult(intent, PantauConstants.RequestCode.FILTER_JANPOL)
                }
                else -> {
                    val intent = Intent(context, FilterKuisActivity::class.java)
                    startActivityForResult(intent, PantauConstants.RequestCode.FILTER_KUIS)
                }
            }
        }
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
                if (tab.position == 0) {
                    btn_create?.visibility = View.VISIBLE
                } else if (tab.position == 1) {
                    btn_create?.visibility = View.GONE
                }
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
        fun newInstance(): PenPolFragment {
            return PenPolFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (tanyaKandidatFragment != null) {
            tanyaKandidatFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }
}