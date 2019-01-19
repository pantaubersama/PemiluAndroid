package com.pantaubersama.app.ui.search.history

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment

class SearchHistoryFragment : CommonFragment() {
    override fun setLayout(): Int = R.layout.fragment_search_history

    companion object {
        val TAG = SearchHistoryFragment::class.java.simpleName

        fun newInstance(): SearchHistoryFragment {
            val fragment = SearchHistoryFragment()
            return fragment
        }
    }

    override fun initView(view: View) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
