package com.pantaubersama.app.ui.search.cluster.filter

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.cluster.Category

/**
 * @author edityomurti on 22/01/2019 20:37
 */
interface FilterClusterView : BaseView {
    fun showSelectedFilter(categoryItem: Category?)
    fun onSuccessSetFilter()
}