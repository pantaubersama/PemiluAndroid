package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.model.accesstoken.TokenResponse
import com.pantaubersama.app.data.model.cluster.CategoryData
import com.pantaubersama.app.data.model.cluster.ClustersResponse
import com.pantaubersama.app.data.model.user.BadgeResponse
import com.pantaubersama.app.data.model.user.Informant
import com.pantaubersama.app.data.model.user.ProfileResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauOAuthAPI {
    @GET("/v1/callback")
    fun exchangeToken(@Query("provider_token") oAuthToken: String?): Single<TokenResponse>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String?,
        @Field("refresh_token") refresh_token: String?
    ): Call<Token>

    @FormUrlEncoded
    @POST("/oauth/revoke")
    fun revokeToken(
        @Field("client_id") client_id: String?,
        @Field("client_secret") client_secret: String?
    ): Completable

    @GET("/v1/me")
    fun getUserProfile(): Single<ProfileResponse>

    @GET("/v1/badges")
    fun getUserBadges(): Single<BadgeResponse>

    @DELETE("/v1/me/clusters")
    fun leaveCluster(): Completable

    @FormUrlEncoded
    @PUT("/v1/me")
    fun updateUserData(
        @Field("full_name") name: String?,
        @Field("username") username: String?,
        @Field("location") location: String?,
        @Field("about") description: String?,
        @Field("education") education: String?,
        @Field("occupation") occupation: String?
    ): Completable

    @Multipart
    @PUT("/v1/me/avatar")
    fun uploadAvatar(
        @Part avatar: MultipartBody.Part?
    ): Completable

    @FormUrlEncoded
    @POST("v1/me/password")
    fun updatePassword(
        @Field("password") password: String,
        @Field("password_confirmation") confirmation: String
    ): Completable

    @GET("v1/me/informants")
    fun getDataLapor(): Single<Informant>

    @FormUrlEncoded
    @PUT("v1/informants")
    fun updateDataLapor(
        @Field("identity_number") idNumber: String?,
        @Field("pob") pob: String?,
        @Field("dob") dob: String?,
        @Field("gender") gender: Int?,
        @Field("occupation") occupation: String?,
        @Field("nationality") nationality: String?,
        @Field("address") address: String?,
        @Field("phone_number") phoneNumber: String?
    ): Completable

    @GET("v1/clusters")
    fun getClusterList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") keyword: String? = "",
        @Query("filter_by") filterBy: String? = "category_id",
        @Query("filter_value") filterValue: String = ""
    ): Single<ClustersResponse>

    @FormUrlEncoded
    @PUT("v1/verifications/ktp_number")
    fun submitKtpNumber(
        @Field("ktp_number") ktpNumber: String?
    ): Completable

    @Multipart
    @PUT("/v1/verifications/ktp_selfie")
    fun submitSelfieKtp(
        @Part avatar: MultipartBody.Part?
    ): Completable

    @Multipart
    @PUT("/v1/verifications/ktp_photo")
    fun submitKtpPhoto(
        @Part ktpPhoto: MultipartBody.Part?
    ): Completable

    @Multipart
    @PUT("/v1/verifications/signature")
    fun submitSignaturePhoto(
        @Part signPhoto: MultipartBody.Part?
    ): Completable

    @FormUrlEncoded
    @POST("/v1/categories")
    fun createNewCategory(
        @Field("name") categoryName: String
    ): Completable

    @GET("/v1/categories")
    fun getCategories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("name")query: String
    ): Single<CategoryData>
}