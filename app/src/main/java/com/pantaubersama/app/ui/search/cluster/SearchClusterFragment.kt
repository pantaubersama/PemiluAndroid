package com.pantaubersama.app.ui.search.cluster

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import kotlinx.android.synthetic.main.fragment_search_cluster.*

class SearchClusterFragment : CommonFragment(), UpdateableFragment {
    private lateinit var keyword: String

    override fun setLayout(): Int = R.layout.fragment_search_cluster

    companion object {
        private val TAG = SearchClusterFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchClusterFragment {
            val fragment = SearchClusterFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_KEYWORD, keyword)
            fragment.arguments = args
            return fragment
        }
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString(EXTRA_SEARCH_KEYWORD)?.let { keyword = it }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        getData(keyword)
    }

    override fun getData(keyword: String) {
        this.keyword = keyword
        /* ganti dengan presenter.getData(keyword) */
        tv_keyword.text = "$keyword in $TAG"
    }
}
