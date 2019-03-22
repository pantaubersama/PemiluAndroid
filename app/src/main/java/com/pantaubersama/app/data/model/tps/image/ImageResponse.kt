package com.pantaubersama.app.data.model.tps.image

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("data")
    var data: ImageData
)