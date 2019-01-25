package com.pantaubersama.app.ui.linimasa.pilpres.filter

import com.pantaubersama.app.base.BaseView

/**
 * @author edityomurti on 21/12/2018 20:48
 */
interface FilterPilpresView : BaseView {
    fun showSelectedFilter(selectedFilter: String)
    fun onSuccessSetFilter()
}