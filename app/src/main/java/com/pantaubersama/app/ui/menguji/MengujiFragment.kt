package com.pantaubersama.app.ui.menguji

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.home.HomeFragment
import com.pantaubersama.app.utils.extensions.color

class MengujiFragment : HomeFragment() {

    companion object {
        val TAG: String = MengujiFragment::class.java.simpleName

        fun newInstance(): MengujiFragment {
            return MengujiFragment()
        }
    }

    override val pagerFragments: List<Pair<Fragment, String>>
        get() = listOf(
            Fragment() to getString(R.string.publik_label),
            Fragment() to getString(R.string.personal_label))

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        changeTopBarColor(isHidden)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        changeTopBarColor(hidden)
    }

    private fun changeTopBarColor(hidden: Boolean) {
        val (appbarColor, statusBarColor) = if (hidden)
            color(R.color.colorPrimary) to color(R.color.colorPrimaryDark)
        else
            color(R.color.yellow) to color(R.color.yellow_dark)
        (requireActivity() as HomeActivity).changeTopBarColor(appbarColor, statusBarColor)
    }
}