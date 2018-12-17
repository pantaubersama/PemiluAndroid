package com.pantaubersama.app.data.api

import com.pantaubersama.app.data.model.accesstoken.Token
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface RestAPI {
    @FormUrlEncoded
    @POST("oauth/token")
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String?,
        @Field("refresh_token") refresh_token: String?
    ): Single<Token>
}