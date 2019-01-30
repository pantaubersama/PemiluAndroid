package com.pantaubersama.app.data.model.partai

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Thumbnail(
    @SerializedName("url")
    var url: String?
) : Serializable