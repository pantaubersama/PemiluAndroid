package com.pantaubersama.app.data.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("title") val title: String? = "",
    @SerializedName("body") val body: String? = ""
) {
    val TAG = "notification"
}

data class PemiluBroadcast(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("event_type") val eventType: String = "",
    @SerializedName("link") val link: String = ""
) {
    companion object {
        val TAG = "pemilu_broadcast"
    }
}