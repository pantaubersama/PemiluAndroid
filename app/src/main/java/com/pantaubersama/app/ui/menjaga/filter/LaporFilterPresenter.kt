package com.pantaubersama.app.ui.menjaga.filter

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.LaporInteractor
import com.pantaubersama.app.data.model.partai.PoliticalParty
import javax.inject.Inject

class LaporFilterPresenter @Inject constructor(private val laporInteractor: LaporInteractor) : BasePresenter<LaporFilterView>() {
    fun getFilter() {
        view?.bindLaporUserFilter(laporInteractor.loadLaporUserFilter())
        view?.bindLaporPartyFilter(laporInteractor.loadLaporPartyFilter())
    }

    fun getSearchFilter() {
        view?.bindLaporUserFilter(laporInteractor.loadLaporUserFilterSearch())
        view?.bindLaporPartyFilter(laporInteractor.loadLaporPartyFilterSearch())
    }

    fun saveLaporFilter(userFilter: String, partyFilter: PoliticalParty) {
        laporInteractor.saveLaporFilter(userFilter, partyFilter)
        view?.onSuccessSaveFilter()
    }

    fun setSearchFilter(userFilter: String, partyFilter: PoliticalParty) {
        laporInteractor.saveLaporFilterSearch(userFilter, partyFilter)
        view?.onSuccessSaveFilter()
    }
}