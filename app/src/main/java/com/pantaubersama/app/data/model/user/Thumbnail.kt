package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Thumbnail(
    @SerializedName("url")
    @Expose
    var url: String? = null
) : Serializable