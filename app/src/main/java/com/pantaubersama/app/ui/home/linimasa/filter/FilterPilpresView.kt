package com.pantaubersama.app.ui.home.linimasa.filter

import com.pantaubersama.app.base.BaseView

/**
 * @author edityomurti on 21/12/2018 20:48
 */
interface FilterPilpresView : BaseView {
    fun showSelectedFilter(selectedFilter: Int)
    fun onSuccessSetFilter()
}