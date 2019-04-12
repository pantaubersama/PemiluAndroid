package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.notification.NotificationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("v1/records/notification")
    fun getNotifications(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("filter_by") filter: String
    ): Single<NotificationResponse>
}