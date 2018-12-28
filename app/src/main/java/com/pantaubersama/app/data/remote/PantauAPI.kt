package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.bannerinfo.BannerInfoResponse
import io.reactivex.Single
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauAPI {
    @GET("linimasa/v1/banner_infos")
    fun getBannerInfos(
        @Header("Authorization") accessToken: String
    ): Single<BannerInfoResponse>
}