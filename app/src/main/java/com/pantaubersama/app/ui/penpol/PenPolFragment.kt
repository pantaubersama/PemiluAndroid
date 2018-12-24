package com.pantaubersama.app.ui.penpol

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.fragment_pen_pol.view.*
/**
 * A simple [Fragment] subclass.
 *
 */
class PenPolFragment : BaseFragment<BasePresenter<*>>() {
    private var mView: View? = null

    private var selectedTabs: Int = 0

    private var tanyaKandidatFragment = TanyaKandidatFragment.newInstance()
    private var kuisFragment = KuisFragment.newInstance()

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
        mView = view
        setupTabLayout()
        setupViewPager()
        view.btn_create.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivity(intent)
        }
        view.btn_filter?.setOnClickListener {
            when (selectedTabs) {
                0 -> {
                    val intent = Intent(context, FilterTanyaKandidatActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    // else
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

        mView?.tab_layout?.addTab(mView?.tab_layout?.newTab()?.setCustomView(tanyaKandidatTab)!!)
        mView?.tab_layout?.addTab(mView?.tab_layout?.newTab()?.setCustomView(kuisTab)!!)

        mView?.tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTabs = tab!!.position
                if (tab.position == 0) {
                    mView?.btn_create?.visibility = View.VISIBLE
                } else if (tab.position == 1) {
                    mView?.btn_create?.visibility = View.GONE
                }
                mView?.view_pager?.currentItem = tab.position
            }
        })
    }

    fun setupViewPager() {
        mView?.view_pager?.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(mView?.tab_layout) {})
        mView?.view_pager?.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> tanyaKandidatFragment
                    1 -> kuisFragment
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return mView?.tab_layout?.tabCount!!
            }
        }
    }

    companion object {
        fun newInstance(): PenPolFragment {
            return PenPolFragment()
        }
    }
}