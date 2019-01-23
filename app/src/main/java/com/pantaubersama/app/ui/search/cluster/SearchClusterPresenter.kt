package com.pantaubersama.app.ui.search.cluster

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 22/01/2019 17:22
 */

class SearchClusterPresenter @Inject constructor (
    private val clusterInteractor: ClusterInteractor
) : BasePresenter<SearchClusterView>() {
    var perPage = 20

    fun getClusterList(keyword: String, page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            clusterInteractor.getClusterList(
                page, perPage, keyword,
                clusterInteractor.getSearchClusterFilter()?.id?.let { it } ?: ""
            )
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it?.size != 0) {
                                view?.showClusters(it)
                            } else {
                                view?.showEmptyData()
                            }
                        } else {
                            view?.showMoreClusters(it)
                        }
                    },
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            view?.onFailedGetData(it)
                        } else {
                            view?.onFailedGetMoreData()
                        }
                        it.printStackTrace()
                        view?.showError(it)
                    }
                )
        )
    }
}