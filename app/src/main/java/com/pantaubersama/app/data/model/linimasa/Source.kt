package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("text")
    var text: String? = null,
    @SerializedName("media")
    var media: MutableList<String>? = null
)