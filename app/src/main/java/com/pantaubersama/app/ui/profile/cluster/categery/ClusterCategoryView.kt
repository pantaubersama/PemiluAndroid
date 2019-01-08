package com.pantaubersama.app.ui.profile.cluster.categery

import com.pantaubersama.app.base.BaseView

interface ClusterCategoryView : BaseView {
    fun showAddCategoryLoading()
    fun disableAddCategoryView()
    fun onSuccessAddNewCategory()
    fun enableAddCategoryView()
    fun showFailedAddCategoryAlert()
    fun dismissAddCategoryLoading()
}