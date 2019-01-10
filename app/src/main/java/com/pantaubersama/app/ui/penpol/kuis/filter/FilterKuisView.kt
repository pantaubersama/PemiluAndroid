package com.pantaubersama.app.ui.penpol.kuis.filter

import com.pantaubersama.app.base.BaseView

interface FilterKuisView : BaseView {
    fun setKuisFilter(kuisFilter: String)
    fun onSuccessSaveKuisFilter()
    fun showSuccessSaveKuisFilterAlert()
    fun showFailedSaveKuisFilterAlert()
}