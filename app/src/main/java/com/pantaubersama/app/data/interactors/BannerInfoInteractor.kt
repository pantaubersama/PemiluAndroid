package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.bannerinfo.BannerInfosResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.PantauConstants
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
    fun getBannerInfoAll(): Single<BannerInfosResponse>? {
        return apiWrapper?.getPantauApi()?.getBannerInfos()
            ?.subscribeOn(rxSchedulers?.io())
            ?.observeOn(rxSchedulers?.mainThread())
    }

    fun getBannerInfo(pageName: String): Single<BannerInfo>? {
        return apiWrapper?.getPantauApi()?.getBannerInfo(pageName)
            ?.subscribeOn(rxSchedulers?.io())
            ?.map { it.data.bannerInfo }
            ?.observeOn(rxSchedulers?.mainThread())
    }

    fun setBannerOpened(pageName: String) {
        when (pageName) {
            PantauConstants.BANNER_PILPRES -> dataCache?.setBannerPilpresOpened(true)
            PantauConstants.BANNER_JANPOL -> dataCache?.setBannerJanpolOpened(true)
            PantauConstants.BANNER_TANYA -> dataCache?.setBannerTanyaKandidatOpened(true)
            PantauConstants.BANNER_KUIS -> dataCache?.setBannerKuisOpened(true)
        }
    }

    fun isBannerPilpresShown(): Boolean? {
        return !dataCache?.isBannerPilpresOpened()!!
    }
}