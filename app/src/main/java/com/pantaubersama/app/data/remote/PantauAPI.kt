package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.bannerinfo.BannerInfoResponse
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauAPI {
    @GET("linimasa/v1/banner_infos")
    fun getBannerInfos(): Single<BannerInfoResponse>
    @FormUrlEncoded
    @POST("pendidikan_politik/v1/questions")
    fun createTanyaKandidat(@Field("body") body: String?): Single<ResponseBody>

    @GET("pendidikan_politik/v1/questions")
    fun getTanyaKandidatList(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?,
        @Query("direction") direction: String?,
        @Query("filter_by") filterBy: String?
    ): Single<TanyaKandidatResponse>
}