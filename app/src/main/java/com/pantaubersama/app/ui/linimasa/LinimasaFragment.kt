package com.pantaubersama.app.ui.linimasa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeFragment
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OPEN_TAB_TYPE
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_PILPRES
import kotlinx.android.synthetic.main.fragment_view_pager.*

class LinimasaFragment : HomeFragment() {

    private var pilpresFragment = PilpresFragment.newInstance()
    private var janjiPolitikFragment = JanjiPolitikFragment()

    private var initialSelectedTab = 0

    override val pagerFragments: List<Pair<Fragment, String>> by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            pilpresFragment to getString(R.string.txt_tab_linimasa),
            janjiPolitikFragment to getString(R.string.txt_tab_janji_politik))
    }

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
            initialSelectedTab = getInt(EXTRA_OPEN_TAB_TYPE, 0)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        view_pager.currentItem = initialSelectedTab
    }

    override val onFilterClicked = { tabPosition: Int ->
        when (tabPosition) {
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
