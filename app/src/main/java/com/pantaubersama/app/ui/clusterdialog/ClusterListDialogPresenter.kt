package com.pantaubersama.app.ui.clusterdialog

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ClusterInteractor
import com.pantaubersama.app.data.model.cluster.ClusterItem
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 01:18
 */
class ClusterListDialogPresenter @Inject constructor(private val clusterInteractor: ClusterInteractor)
    : BasePresenter<ClusterListDialogView>() {

    var perPage = 20

    fun getClusterList(page: Int) {
        if (page == 1) {
            view?.showLoading()
        }
//        val clusterList: MutableList<ClusterItem> = ArrayList()
//
//        for (i in 1..10) {
//            val cluster = ClusterItem()
//            cluster.id = "123$i"
//            cluster.name = "Cluster $i"
//            cluster.memberCount = i
//            clusterList.add(cluster)
//        }
//        view?.showClusters(clusterList)

        disposables?.add(clusterInteractor.getClusterList(page, perPage)
            ?.subscribe(
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        if (it?.size != 0) {
                            view?.showClusters(it)
                        } else {
                            view?.showEmptyCluster()
                        }
                    } else {
                        view?.showMoreClusters(it)
                    }
                },
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        view?.showFailedGetClusters()
                    } else {
                        view?.showFailedGetMoreClusters()
                    }
                    it.printStackTrace()
                    view?.showError(it)
                }
            )!!
        )
    }
}