package com.pantaubersama.app.ui.penpol.tanyakandidat

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment

/**
 * @author edityomurti on 18/12/2018 00:56
 */
class PenPolPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var tabCount: Int? = null

    constructor(fm: FragmentManager?, tabCount: Int) : this(fm) {
        this.tabCount = tabCount
    }

    override fun getCount(): Int {
        return tabCount!!
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> TanyaKandidatFragment()
            1 -> KuisFragment()
            else -> null
        }
    }
}