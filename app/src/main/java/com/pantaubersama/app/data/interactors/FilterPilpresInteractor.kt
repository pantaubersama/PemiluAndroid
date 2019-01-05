package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.utils.PantauConstants.Filter.Pilpres.FILTER_ALL
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 20:44
 */
class FilterPilpresInteractor @Inject constructor(
    private val dataCache: DataCache?
) {
    fun getPilpresFilter(): String {
        return dataCache?.getFilterPilpres() ?: FILTER_ALL
    }

    fun setPilpresFilter(selectedFilter: String) {
        dataCache?.setFilterPilpres(selectedFilter)!!
    }
}