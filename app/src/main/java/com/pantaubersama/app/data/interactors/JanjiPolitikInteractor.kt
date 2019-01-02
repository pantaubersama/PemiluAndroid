package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:15
 */
class JanjiPolitikInteractor @Inject constructor(
    private val dataCache: DataCache?
) {
    fun isBannerShown(): Boolean? {
        return dataCache?.isBannerJanpolOpened()
    }
}