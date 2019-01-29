package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

class LaporInteractor @Inject constructor(private val dataCache: DataCache) {
    fun loadLaporUserFilter(): String {
        return dataCache.loadLaporUserFilter()
    }

    fun loadLaporUserFilterSearch(): String {
        return dataCache.loadLaporUserFilterSearch()
    }
}