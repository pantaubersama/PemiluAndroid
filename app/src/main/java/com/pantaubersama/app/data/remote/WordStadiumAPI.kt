package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.model.debat.ChallengeItemResponse
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

    @FormUrlEncoded
    @POST("word_stadium/v1/challenges/direct")
    fun directChallenge(
        @Field("topic_list") topicList: String?,
        @Field("statement") statement: String?,
        @Field("statement_source") statementSource: String?,
        @Field("show_time_at") showTimeAt: String?,
        @Field("time_limit") timeLimit: Int,
        @Field("invitation_id") invitationId: String?,
        @Field("screen_name") screenName: String?
    ): Completable

    @GET("word_stadium/v1/challenges/all")
    fun getPublicChallenge(@Query("progress") progress: String): Single<ChallengeResponse>

    @GET("word_stadium/v1/challenges/me")
    fun getPersonalChallenge(@Query("progress") progress: String): Single<ChallengeResponse>

    @FormUrlEncoded
    @PUT("word_stadium/v1/challenges/open/opponent_candidates")
    fun confirmOpponentCandidate(
        @Field("id") challengeId: String,
        @Field("audience_id") audienceId: String
    ): Completable

    @FormUrlEncoded
    @PUT("word_stadium/v1/challenges/open/ask_as_opponent")
    fun askAsOpponent(@Field("id") challengeId: String): Completable

    @GET("word_stadium/v1/challenges/{id}")
    fun getChallengeItem(@Path("id") id: String): Single<ChallengeItemResponse>
}