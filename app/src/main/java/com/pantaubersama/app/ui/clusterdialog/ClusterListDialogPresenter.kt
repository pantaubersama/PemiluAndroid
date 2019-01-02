package com.pantaubersama.app.ui.clusterdialog

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.cluster.ClusterItem
import timber.log.Timber
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 01:18
 */
class ClusterListDialogPresenter @Inject constructor() : BasePresenter<ClusterListDialogView>() {
    fun getClusterList() {

        Timber.d("clusterList size: getClusterList")
        view?.showLoading()

        val clusterList: MutableList<ClusterItem> = ArrayList()

        for (i in 1..10) {
            val cluster = ClusterItem()
            cluster.id = "123$i"
            cluster.name = "Cluster $i"
//            cluster.memberCount = i
            clusterList.add(cluster)
        }

        view?.dismissLoading()
        view?.showCluster(clusterList)
    }
}