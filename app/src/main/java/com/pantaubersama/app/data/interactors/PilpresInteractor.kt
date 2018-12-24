package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 17:28
 */
class PilpresInteractor @Inject constructor(
    private val dataCache: DataCache?
) {
    fun getPilpresFilter(): Int {
        return dataCache?.getFilterPilpres()!!
    }
}