package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.bannerinfo.BannerInfoResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 21:34
 */
class BannerInfoInteractor @Inject constructor(
    private val apiWrapper: APIWrapper?,
    private val rxSchedulers: RxSchedulers?,
    private val dataCache: DataCache?
) {
    fun getBannerInfo(): Single<BannerInfoResponse>? {
        return apiWrapper?.getPantauApi()?.getBannerInfos()
            ?.subscribeOn(rxSchedulers?.io())
            ?.observeOn(rxSchedulers?.mainThread())
    }
}