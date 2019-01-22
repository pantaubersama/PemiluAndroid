package com.pantaubersama.app.ui.search.cluster

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.ClusterItem

interface SearchClusterView : BaseView {
    fun showClusters(clusterList: MutableList<ClusterItem>)
    fun showMoreClusters(clusterList: MutableList<ClusterItem>)
    fun showEmptyData()
    fun onFailedGetData(throwable: Throwable)
    fun onFailedGetMoreData()
}