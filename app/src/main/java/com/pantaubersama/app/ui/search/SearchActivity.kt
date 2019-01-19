package com.pantaubersama.app.ui.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.search.cluster.SearchClusterFragment
import com.pantaubersama.app.ui.search.janjipolitik.SearchJanjiPolitikFragment
import com.pantaubersama.app.ui.search.linimasa.SearchLinimasaFragment
import com.pantaubersama.app.ui.search.person.SearchPersonFragment
import com.pantaubersama.app.ui.search.quiz.SearchQuizFragment
import com.pantaubersama.app.ui.search.tanya.SearchQuestionFragment
import com.pantaubersama.app.ui.search.wordstadium.SearchWordstadiumFragment
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.activity_search.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.pantaubersama.app.ui.search.history.SearchHistoryFragment

class SearchActivity : CommonActivity() {
    private var keyword: String? = null

//    private var searchPersonFragment = SearchPersonFragment()
//    private var searchClusterFragment = SearchClusterFragment()
//    private var searchLinimasaFragment = SearchLinimasaFragment()
//    private var searchJanpolFragment = SearchJanjiPolitikFragment()
//    private var searchQuestionFragment = SearchQuestionFragment()
//    private var searchQuizFragment = SearchQuizFragment()
//    private var searchWordstadiumFragment = SearchWordstadiumFragment()

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_search

    override fun setupUI(savedInstanceState: Bundle?) {
        setupTabLayout()
        setupViewPager()

        btn_back.setOnClickListener { onBackPressed() }
        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (textView.text.toString().isNotEmpty() && textView.text.toString().isNotBlank()) {
                    performSearch(textView.text.toString())
                    val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    private fun performSearch(keyword: String) {
        this.keyword = keyword

        view_pager.setCurrentItem(0, false)
        view_pager.adapter?.notifyDataSetChanged()
    }

    private fun setupTabLayout() {
        val tabOrang = TabView(this)
        tabOrang.setTitleLabel(R.string.txt_tab_orang)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabOrang))
        val tabCluster = TabView(this)
        tabCluster.setTitleLabel(R.string.txt_tab_cluster)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabCluster))
        val tabLinimasa = TabView(this)
        tabLinimasa.setTitleLabel(R.string.txt_tab_linimasa)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabLinimasa))
        val tabJanPol = TabView(this)
        tabJanPol.setTitleLabel(R.string.txt_tab_janji_politik)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabJanPol))
        val tabTanya = TabView(this)
        tabTanya.setTitleLabel(R.string.txt_tab_tanya)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabTanya))
        val tabQuiz = TabView(this)
        tabQuiz.setTitleLabel(R.string.txt_tab_quiz)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabQuiz))
        val tabWordstadium = TabView(this)
        tabWordstadium.setTitleLabel(R.string.txt_tab_wordstadium)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tabWordstadium))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
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
                return tab_layout.tabCount
            }

            override fun getItem(position: Int): Fragment {
                keyword?.let {
                    return when (position) {
                        0 -> SearchPersonFragment.newInstance(it)
                        1 -> SearchClusterFragment.newInstance(it)
                        2 -> SearchLinimasaFragment.newInstance(it)
                        3 -> SearchJanjiPolitikFragment.newInstance(it)
                        4 -> SearchQuestionFragment.newInstance(it)
                        5 -> SearchQuizFragment.newInstance(it)
                        6 -> SearchWordstadiumFragment.newInstance(it)
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
