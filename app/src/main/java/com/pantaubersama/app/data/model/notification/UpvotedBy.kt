package com.pantaubersama.app.data.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.wordstadium.Avatar

data class UpvotedBy(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("full_name")
    @Expose
    var fullName: String,
    @SerializedName("avatar")
    @Expose
    var avatar: Avatar
)