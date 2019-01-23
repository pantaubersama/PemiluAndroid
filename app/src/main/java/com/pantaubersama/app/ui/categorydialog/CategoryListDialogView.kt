package com.pantaubersama.app.ui.categorydialog

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.Category

/**
 * @author edityomurti on 23/01/2019 10:50
 */
interface CategoryListDialogView : BaseView {
    fun showCategory(categories: MutableList<Category>)
    fun showMoreCategory(categories: MutableList<Category>)
    fun onEmptyData()
    fun onFailedGetCategoriesAlert()
}