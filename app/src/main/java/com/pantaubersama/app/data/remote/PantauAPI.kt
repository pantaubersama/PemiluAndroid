package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.bannerinfo.BannerInfoResponse
import com.pantaubersama.app.data.model.bannerinfo.BannerInfosResponse
import com.pantaubersama.app.data.model.linimasa.FeedsResponse
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauAPI {
    @GET("linimasa/v1/banner_infos")
    fun getBannerInfos(): Single<BannerInfosResponse>

    @GET("linimasa/v1/banner_infos/show")
    fun getBannerInfo(
        @Query("page_name") pageName: String?
    ): Single<BannerInfoResponse>

    @GET("linimasa/v1/feeds/pilpres")
    fun getFeeds(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Single<FeedsResponse>

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/questions")
    fun createTanyaKandidat(@Field("body") body: String?): Single<TanyaKandidatResponse>

    @GET("pendidikan_politik/v1/questions")
    fun getTanyaKandidatList(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?,
        @Query("direction") direction: String?,
        @Query("filter_by") filterBy: String?
    ): Single<TanyaKandidatResponse>

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/votes")
    fun upVoteQuestion(
        @Field("id") id: String?,
        @Field("class_name") className: String?
    ): Completable

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/reports")
    fun reportQuestion(
        @Field("id") id: String?,
        @Field("class_name") className: String?
    ): Completable

    @DELETE("pendidikan_politik/v1/questions")
    fun deleteQuestion(
        @Query("id") id: String?
    ): Completable
}