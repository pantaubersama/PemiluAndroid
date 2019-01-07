package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.linimasa.FeedsResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 21/12/2018 17:28
 */
class PilpresInteractor @Inject constructor(
    private val apiWrapper: APIWrapper?,
    private val rxSchedulers: RxSchedulers?
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
}