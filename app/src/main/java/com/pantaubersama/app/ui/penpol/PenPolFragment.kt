package com.pantaubersama.app.ui.penpol

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeFragment
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OPEN_TAB_TYPE
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_KUIS
import kotlinx.android.synthetic.main.activity_search.*

class PenPolFragment : HomeFragment() {

    private var tanyaKandidatFragment: TanyaKandidatFragment = TanyaKandidatFragment.newInstance()
    private var kuisFragment: KuisFragment = KuisFragment.newInstance()

    private var initialSelectedTab = 0

    override val pagerFragments: List<Pair<Fragment, String>> by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            tanyaKandidatFragment to getString(R.string.tanya_kandidat_label),
            kuisFragment to getString(R.string.kata_kandidat_label))
    }

    override val onFilterClicked = { tabPosition: Int ->
        when (tabPosition) {
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

    override fun fetchArguments(args: Bundle?) {
        args?.apply {
            initialSelectedTab = getInt(EXTRA_OPEN_TAB_TYPE, 0)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        view_pager.currentItem = initialSelectedTab
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tanyaKandidatFragment.onActivityResult(requestCode, resultCode, data)
        kuisFragment.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = PenPolFragment::class.java.simpleName

        fun newInstance(): PenPolFragment {
            return PenPolFragment()
        }

        fun newInstanceOpenKuis(): PenPolFragment {
            val fragment = PenPolFragment()
            val args = Bundle()
            args.putInt(EXTRA_OPEN_TAB_TYPE, EXTRA_TYPE_KUIS)
            fragment.arguments = args
            return fragment
        }
    }
}