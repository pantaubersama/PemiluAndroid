package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.model.accesstoken.TokenResponse
import com.pantaubersama.app.data.model.user.BadgeResponse
import com.pantaubersama.app.data.model.user.ProfileResponse
import io.reactivex.Completable
import io.reactivex.Single
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
            @Field("client_secret") client_secret: String?,
            @Field("token") token: String?
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
}