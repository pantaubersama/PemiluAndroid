package com.pantaubersama.app.ui.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.search.linimasa.SearchLinimasaFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.activity_search.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.search.cluster.SearchClusterFragment
import com.pantaubersama.app.ui.search.history.SearchHistoryFragment
import com.pantaubersama.app.ui.search.janjipolitik.SearchJanjiPolitikFragment
import com.pantaubersama.app.utils.extensions.visibleIf
import javax.inject.Inject

class SearchActivity : BaseActivity<SearchPresenter>(), BaseView {
    private var keyword: String? = null

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_search

    @Inject override lateinit var presenter: SearchPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()

        btn_back.setOnClickListener { onBackPressed() }
        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (textView.text.toString().isNotEmpty() && textView.text.toString().isNotBlank()) {
                    performSearch(textView.text.toString())
                    val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)
                    return@setOnEditorActionListener true
//                }
            }
            false
        }
    }

    fun setKeyword(keyword: String) {
        et_search.setText(keyword)
        performSearch(keyword)
    }

    private fun performSearch(keyword: String) {
        this.keyword = keyword
//        view_pager.setCurrentItem(0, false)
        tab_layout.visibleIf(true)
        view_pager.adapter?.notifyDataSetChanged()

        if (keyword.isNotEmpty() and keyword.isNotBlank()) presenter.saveKeyword(keyword)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupTabLayout() {
//        val tabOrang = TabView(this)  // yg belum selesai di hide dulu untuk production @edityo 21/01/19
//        tabOrang.setTitleLabel(R.string.txt_tab_orang)
//        tab_layout.addTab(tab_layout.newTab().setCustomView(tabOrang))
        val tabCluster = TabView(this)
        tabCluster.setTitleLabel(R.string.txt_tab_cluster)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabCluster))
        val tabLinimasa = TabView(this)
        tabLinimasa.setTitleLabel(R.string.txt_tab_linimasa)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabLinimasa))
        val tabJanPol = TabView(this)
        tabJanPol.setTitleLabel(R.string.txt_tab_janji_politik)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabJanPol))
//        val tabTanya = TabView(this)
//        tabTanya.setTitleLabel(R.string.txt_tab_tanya)
//        tab_layout.addTab(tab_layout.newTab().setCustomView(tabTanya))
//        val tabQuiz = TabView(this)
//        tabQuiz.setTitleLabel(R.string.txt_tab_quiz)
//        tab_layout.addTab(tab_layout.newTab().setCustomView(tabQuiz))
//        val tabWordstadium = TabView(this)
//        tabWordstadium.setTitleLabel(R.string.txt_tab_wordstadium)
//        tab_layout.addTab(tab_layout.newTab().setCustomView(tabWordstadium))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val currentFragment = supportFragmentManager.findFragmentByTag(
                    "android:switcher:" + R.id.view_pager + ":" + view_pager.currentItem
                ) as CommonFragment?

                currentFragment?.scrollToTop()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { view_pager.currentItem = it }
            }
        })
    }

    private fun setupViewPager() {
        view_pager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tab_layout) {})
        view_pager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return keyword?.let { tab_layout.tabCount } ?: 1
            }

            override fun getItem(position: Int): Fragment {
                keyword?.let {
                    return when (position) {
//                        0 -> SearchPersonFragment.newInstance(it)
//                        1 -> SearchClusterFragment.newInstance(it)
//                        2 -> SearchLinimasaFragment.newInstance(it)
//                        3 -> SearchJanjiPolitikFragment.newInstance(it)
//                        4 -> SearchQuestionFragment.newInstance(it)
//                        5 -> SearchQuizFragment.newInstance(it)
//                        6 -> SearchWordstadiumFragment.newInstance(it)

                        0 -> SearchClusterFragment.newInstance(it)
                        1 -> SearchLinimasaFragment.newInstance(it) // sementara hanya untuk production @edityo 21/01/19
                        2 -> SearchJanjiPolitikFragment.newInstance(it)
                        else -> Fragment()
                    }
                } ?: return SearchHistoryFragment.newInstance()
            }

            override fun getItemPosition(`object`: Any): Int {
                return if (`object` is UpdateableFragment) {
                    keyword?.let { `object`.getData(it) }
                    super.getItemPosition(`object`)
                } else {
                    PagerAdapter.POSITION_NONE
                }
            }
        }
    }
}
