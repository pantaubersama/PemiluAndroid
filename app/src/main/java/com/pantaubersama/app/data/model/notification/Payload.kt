package com.pantaubersama.app.data.model.notification

import com.google.gson.annotations.SerializedName

data class Notificatioon(
    @SerializedName("title") val title: String? = "",
    @SerializedName("body") val body: String? = ""
) {
    val TAG = "notification"
}

data class PemiluBroadcast(
    @SerializedName("id") val id: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("notif_type") val notifType: String = "",
    @SerializedName("event_type") val eventType: String = "",
    @SerializedName("link") val link: String = "pantaubersama.com"
) {
    val TAG = "pemilu_broadcast"
}