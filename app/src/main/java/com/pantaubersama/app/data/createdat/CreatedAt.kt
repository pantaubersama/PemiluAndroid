package com.pantaubersama.app.data.createdat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatedAt(
    @SerializedName("iso_8601")
    @Expose
    var iso8601: String,
    @SerializedName("en")
    @Expose
    var en: String,
    @SerializedName("id")
    @Expose
    var id: String
) : Serializable