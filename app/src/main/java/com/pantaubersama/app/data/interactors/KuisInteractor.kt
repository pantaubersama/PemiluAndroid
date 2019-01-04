package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import io.reactivex.Completable
import javax.inject.Inject

class KuisInteractor @Inject constructor(
    private val dataCache: DataCache
) {
    fun isBannerShown(): Boolean? {
        return dataCache.isBannerKuisOpened()
    }

    fun getKuisFilter(): String? {
        return dataCache.getKuisFilter()
    }

    fun saveKuisFilter(kuisFilter: String?): Completable {
        return Completable.fromCallable {
            dataCache.saveKuisFilter(kuisFilter!!)
        }
    }
}