package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null,
    @SerializedName("username")
    @Expose
    var username: String? = null,
    @SerializedName("avatar")
    @Expose
    var avatar: Avatar? = null,
    @SerializedName("verified")
    @Expose
    var verified: Boolean? = false,
    @SerializedName("about")
    @Expose
    var about: String? = null
) : Serializable