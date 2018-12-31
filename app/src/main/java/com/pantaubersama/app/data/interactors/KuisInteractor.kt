package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

class KuisInteractor @Inject constructor(
    private val dataCache: DataCache?
) {
    fun isBannerShown(): Boolean? {
        return dataCache?.isBannerKuisOpened()
    }
}