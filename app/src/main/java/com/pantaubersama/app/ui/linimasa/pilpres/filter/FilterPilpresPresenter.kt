package com.pantaubersama.app.ui.linimasa.pilpres.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PilpresInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 20:47
 */
class FilterPilpresPresenter @Inject constructor(
    private val pilpresInteractor: PilpresInteractor?
) : BasePresenter<FilterPilpresView>() {

    fun getFilter() {
        view?.showSelectedFilter(pilpresInteractor?.getPilpresFilter()!!)
    }

    fun setFilter(selectedFilter: String) {
        pilpresInteractor?.setPilpresFilter(selectedFilter)
        view?.onSuccessSetFilter()
    }
}