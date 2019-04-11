package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.notification.NotificationResponse
import io.reactivex.Single
import retrofit2.http.GET

interface NotificationApi {
    @GET("dashboard/v1/notifications")
    fun getNotifications(): Single<NotificationResponse>
}