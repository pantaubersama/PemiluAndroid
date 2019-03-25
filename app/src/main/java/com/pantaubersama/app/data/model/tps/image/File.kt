package com.pantaubersama.app.data.model.tps.image

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.image.Thumbnail

data class File(
    @SerializedName("url")
    var url: String,
    @SerializedName("thumbnail")
    var thumbnail: Thumbnail
)