package com.pantaubersama.app.ui.menjaga.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LaporInteractor
import javax.inject.Inject

class LaporFilterPresenter @Inject constructor(private val laporInteractor: LaporInteractor) : BasePresenter<LaporFilterView>() {
    fun getFilter() {
        view?.bindLaporUserFilter(laporInteractor.loadLaporUserFilter())
    }

    fun getSearchFilter() {
        view?.bindLaporUserFilter(laporInteractor.loadLaporUserFilterSearch())
    }
}