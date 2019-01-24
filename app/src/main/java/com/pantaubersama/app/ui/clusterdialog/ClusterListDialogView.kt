package com.pantaubersama.app.ui.clusterdialog

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.ClusterItem

/**
 * @author edityomurti on 27/12/2018 01:16
 */
interface ClusterListDialogView : BaseView {
    fun showClusters(clusterList: MutableList<ClusterItem>)
    fun showMoreClusters(clusterList: MutableList<ClusterItem>)
    fun showEmptyCluster()
    fun showFailedGetClusters()
    fun showFailedGetMoreClusters()
}