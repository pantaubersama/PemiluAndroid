package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id") var id: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("profile_image_url") var profileImageUrl: String?
)