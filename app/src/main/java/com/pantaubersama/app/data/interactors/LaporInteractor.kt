package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.partai.PoliticalParty
import javax.inject.Inject

class LaporInteractor @Inject constructor(private val dataCache: DataCache) {
    fun loadLaporUserFilter(): String {
        return dataCache.loadLaporUserFilter()
    }

    fun loadLaporUserFilterSearch(): String {
        return dataCache.loadLaporUserFilterSearch()
    }

    fun loadLaporPartyFilter(): PoliticalParty? {
        return dataCache.loadLaporPartyFilter()
    }

    fun loadLaporPartyFilterSearch(): PoliticalParty? {
        return dataCache.loadLaporPartyFilterSearch()
    }

    fun saveLaporFilter(userFilter: String, partyFilter: PoliticalParty) {
        dataCache.saveLaporUserFilter(userFilter)
        dataCache.saveLaporPartyFilter(partyFilter)
    }

    fun saveLaporFilterSearch(userFilter: String, partyFilter: PoliticalParty) {
        dataCache.saveLaporUserFilterSearch(userFilter)
        dataCache.saveLaporPartyFilterSearch(partyFilter)
    }
}