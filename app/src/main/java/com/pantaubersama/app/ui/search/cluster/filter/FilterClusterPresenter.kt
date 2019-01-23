package com.pantaubersama.app.ui.search.cluster.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import com.pantaubersama.app.data.model.cluster.Category
import javax.inject.Inject

/**
 * @author edityomurti on 22/01/2019 20:37
 */
class FilterClusterPresenter @Inject constructor(
    private val clusterInteractor: ClusterInteractor
) : BasePresenter<FilterClusterView>() {
    fun getFilter() {
        view?.showSelectedFilter(clusterInteractor.getSearchClusterFilter())
    }

    fun setFilter(categoryItem: Category) {
        clusterInteractor.setSearchClusterFilter(categoryItem)
        view?.onSuccessSetFilter()
    }
}