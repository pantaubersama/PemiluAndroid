package com.pantaubersama.app.ui.clusterdetail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.ClusterItem

interface ClusterDetailView : BaseView {
    fun showFailedGetClusterAlert()
    fun bindClusterData(clusterItem: ClusterItem)
}