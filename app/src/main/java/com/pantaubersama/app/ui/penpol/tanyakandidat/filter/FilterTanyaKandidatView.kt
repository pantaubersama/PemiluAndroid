package com.pantaubersama.app.ui.penpol.tanyakandidat.filter

import com.pantaubersama.app.base.BaseView

interface FilterTanyaKandidatView : BaseView {
    fun onSuccessSaveFilter()
    fun showSuccessSaveFilterAlert()
    fun showFailedSaveFilterAlert()
    fun setUserFilter(userfilter: String?)
    fun showFailedLoadUserfilter()
    fun setOrderFilter(orderFilter: String?)
    fun showFailedLoadOrderfilter()

}