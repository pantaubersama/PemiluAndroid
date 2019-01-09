package com.pantaubersama.app.ui.profile.cluster.requestcluster

import com.pantaubersama.app.base.BaseView

interface RequestClusterView : BaseView {
    fun disableView()
    fun showSuccessRequestClusterAlert()
    fun onSuccessRequestAlert()
    fun enableView()
    fun showFailedRequestClusterAlert()
}