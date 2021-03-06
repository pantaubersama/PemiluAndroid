package com.pantaubersama.app.ui.penpol.kuis.filter

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface FilterKuisView : BaseView {
    fun setKuisFilter(kuisFilter: String)
    fun onSuccessSaveKuisFilter()
    fun showSuccessSaveKuisFilterAlert()
    fun showFailedSaveKuisFilterAlert()
    fun bindProfile(profile: Profile)
}