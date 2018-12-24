package com.pantaubersama.app.ui.home.linimasa.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.FilterPilpresInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 20:47
 */
class FilterPilpresPresenter @Inject constructor(private val filterPilpresInteractor: FilterPilpresInteractor?) : BasePresenter<FilterPilpresView>() {

    fun getFilter() {
        view?.showSelectedFilter(filterPilpresInteractor?.getPilpresFilter()!!)
    }

    fun setFilter(selectedFilter: Int) {
        filterPilpresInteractor?.setPilpresFilter(selectedFilter)
        view?.onSuccessSetFilter()
    }
}