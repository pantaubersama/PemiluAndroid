package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.Token
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface WordStadiumAPI {
    @FormUrlEncoded
    @POST("oauth/token")
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String?,
        @Field("refresh_token") refresh_token: String?
    ): Single<Token>

    @FormUrlEncoded
    @POST("word_studium/v1/challenges/open")
    fun openChallenge(
        @Field("Authorization") accessToken: String?,
        @Field("topic_list") topicList: String?,
        @Field("statement") statement: String?,
        @Field("statement_source") statementSource: String?,
        @Field("show_time_at") showTimeAt: String?,
        @Field("time_limit") timeLimit: Int
    ): Completable
}