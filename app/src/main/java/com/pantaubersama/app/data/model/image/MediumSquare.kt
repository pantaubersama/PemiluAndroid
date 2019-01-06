package com.pantaubersama.app.data.model.image

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MediumSquare(
    @SerializedName("url")
    @Expose
    var url: String? = null
) : Serializable