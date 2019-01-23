package com.pantaubersama.app.ui.linimasa.janjipolitik.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.model.cluster.ClusterItem
import javax.inject.Inject

class FilterJanjiPolitikPresenter @Inject constructor(
    private val janpolInteractor: JanjiPolitikInteractor
) : BasePresenter<FilterJanjiPolitikView>() {
    fun getFilter() {
        view?.showSelectedFilter(janpolInteractor.getJanpolUserFilter(), janpolInteractor.getJanpolClusterFilter())
    }

    fun getSearchFilter() {
        view?.showSelectedFilter(janpolInteractor.getSearchJanpolUserFilter(), janpolInteractor.getSearchJanpolClusterFilter())
    }

    fun setFilter(userFilter: String, clusterFilter: ClusterItem?) {
        janpolInteractor.setJanpolFilter(userFilter, clusterFilter)
        view?.onSuccessSetFilter()
    }

    fun setSearchFilter(userFilter: String, clusterFilter: ClusterItem?) {
        janpolInteractor.setSearchJanpolFilter(userFilter, clusterFilter)
        view?.onSuccessSetFilter()
    }
}