package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.linimasa.FeedsResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 17:28
 */
class PilpresInteractor @Inject constructor(
    private val apiWrapper: APIWrapper?,
    private val rxSchedulers: RxSchedulers?,
    private val dataCache: DataCache?
) {
    fun getFeeds(
        filterBy: String,
        page: Int,
        perPage: Int
    ): Single<FeedsResponse>? {
        return apiWrapper?.getPantauApi()?.getFeeds(filterBy, page, perPage)
            ?.subscribeOn(rxSchedulers?.io())
            ?.observeOn(rxSchedulers?.mainThread())
    }

    fun getPilpresFilter(): String {
        return dataCache?.getFilterPilpres()!!
    }

    fun setPilpresFilter(selectedFilter: String) {
        dataCache?.saveFilterPilpres(selectedFilter)!!
    }
}