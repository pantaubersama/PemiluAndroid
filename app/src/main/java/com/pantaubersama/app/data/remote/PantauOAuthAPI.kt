package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.TokenResponse
import io.reactivex.Single
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
    ): Single<TokenResponse>
}