package com.pantaubersama.app.ui.merayakan

import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeFragment
import com.pantaubersama.app.ui.merayakan.perhitungan.PerhitunganFragment
import com.pantaubersama.app.ui.merayakan.rekapitulasi.RekapitulasiFragment

class MerayakanFragment : HomeFragment() {

    companion object {
        val TAG = MerayakanFragment::class.java.simpleName

        fun newInstance(): MerayakanFragment {
            return MerayakanFragment()
        }
    }

    override val pagerFragments: List<Pair<Fragment, String>>
        get() = listOf(
            PerhitunganFragment.newInstance() to getString(R.string.perhitungan_label),
            RekapitulasiFragment.newInstance() to getString(R.string.rekapitulasi_label)
        )
}
