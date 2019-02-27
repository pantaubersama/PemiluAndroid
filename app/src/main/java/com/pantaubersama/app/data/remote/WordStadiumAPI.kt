package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.model.debat.ChallengeResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

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
    @POST("word_stadium/v1/challenges/open")
    fun openChallenge(
        @Field("topic_list") topicList: String?,
        @Field("statement") statement: String?,
        @Field("statement_source") statementSource: String?,
        @Field("show_time_at") showTimeAt: String?,
        @Field("time_limit") timeLimit: Int
    ): Completable

    @GET("word_stadium/v1/challenges/all")
    fun getPublicChallenge(@Query("progress") progress: String): Single<ChallengeResponse>

    @GET("word_stadium/v1/challenges/me")
    fun getPersonalChallenge(@Query("progress") progress: String): Single<ChallengeResponse>
}