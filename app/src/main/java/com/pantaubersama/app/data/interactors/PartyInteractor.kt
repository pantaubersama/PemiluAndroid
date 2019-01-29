package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class PartyInteractor @Inject constructor(
    private val dataCache: DataCache,
    private val rxSchedulers: RxSchedulers,
    private val apiWrapper: APIWrapper
) {
    fun getParties(page: Int, perPage: Int): Single<List<PoliticalParty>> {
        return apiWrapper.getPantauOAuthApi().getPartai(page, perPage)
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                response.data.politicalParties
            }
            .observeOn(rxSchedulers.mainThread())
    }
}