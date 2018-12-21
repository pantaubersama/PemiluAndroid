package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 20:44
 */
class FilterPilpresInteractor @Inject constructor(
    private val dataCache: DataCache?
) {
    fun getPilpresFilter(): Int {
        return dataCache?.getFilterPilpres()!!
    }

    fun setPilpresFilter(selectedFilter: Int) {
        dataCache?.setFilterPilpres(selectedFilter)!!
    }
}