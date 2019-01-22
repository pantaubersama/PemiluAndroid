package com.pantaubersama.app.ui.search.person.filter

import com.pantaubersama.app.base.BaseView

interface FilterOrangView : BaseView {
    fun setFilter(searchOrangFilter: String)
    fun onSuccessSaveFilter()
    fun showSuccessSaveFilterAlert()
    fun showFailedSaveFilterAlert()
}