package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitikData
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:15
 */
class JanjiPolitikInteractor @Inject constructor(
    private val apiWrapper: APIWrapper?,
    private val rxSchedulers: RxSchedulers?,
    private val dataCache: DataCache?
) {
    private fun getJanpolUserFilter(): String {
        return dataCache?.getJanpolUserFilter()!!
    }

    private fun getJanpolClusterFilter(): String {
        return dataCache?.getJanpolClusterFilter()!!
    }

    fun getJanPol(
        keyword: String?,
        page: Int?,
        perPage: Int?
    ): Single<JanjiPolitikData?>? {
        return apiWrapper?.getPantauApi()
            ?.getJanPol(keyword, getJanpolClusterFilter(), getJanpolUserFilter(), page, perPage)
            ?.subscribeOn(rxSchedulers?.io())
            ?.map { it.data }
            ?.observeOn(rxSchedulers?.mainThread())
    }
}