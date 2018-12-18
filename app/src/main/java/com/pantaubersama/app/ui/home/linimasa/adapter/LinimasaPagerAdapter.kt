package com.pantaubersama.app.ui.home.linimasa.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pantaubersama.app.ui.home.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.home.linimasa.pilpres.PilpresFragment

/**
 * @author edityomurti on 18/12/2018 00:56
 */
class LinimasaPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var tabCount: Int? = null

    constructor(fm: FragmentManager?, tabCount: Int) : this(fm) {
        this.tabCount = tabCount
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> PilpresFragment()
            1 -> JanjiPolitikFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return tabCount!!
    }
}