package com.pantaubersama.app.ui.linimasa.janjipolitik.filter

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.ClusterItem

interface FilterJanjiPolitikView : BaseView {
    fun showSelectedFilter(userFilter: String, clusterFilter: ClusterItem?)
    fun onSuccessSetFilter()
}