package com.pantaubersama.app.ui.profile.cluster.categery

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.Category

interface ClusterCategoryView : BaseView {
    fun showAddCategoryLoading()
    fun disableAddCategoryView()
    fun onSuccessAddNewCategory()
    fun enableAddCategoryView()
    fun showFailedAddCategoryAlert()
    fun dismissAddCategoryLoading()
    fun bindData(categories: MutableList<Category>)
    fun showFailedGetCategoriesAlert()
    fun showEmptyAlert()
    fun bindNextData(categories: MutableList<Category>)
    fun setDataEnd()
}