package com.pantaubersama.app.ui.search.wordstadium

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import kotlinx.android.synthetic.main.fragment_search_wordstadium.*

class SearchWordstadiumFragment : CommonFragment(), UpdateableFragment {
    private lateinit var keyword: String
    override fun setLayout(): Int = R.layout.fragment_search_wordstadium

    companion object {
        private val TAG = SearchWordstadiumFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchWordstadiumFragment {
            val fragment = SearchWordstadiumFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_KEYWORD, keyword)
            fragment.arguments = args
            return fragment
        }
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString(EXTRA_SEARCH_KEYWORD)?.let { keyword = it }
    }

    override fun initView(view: View) {
        getData(keyword)
    }

    override fun getData(keyword: String) {
        /* ganti dengan presenter.getData(keyword) */
        tv_keyword.text = "$keyword in $TAG"
    }
}
