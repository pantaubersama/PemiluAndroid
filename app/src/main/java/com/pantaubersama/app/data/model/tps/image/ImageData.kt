package com.pantaubersama.app.data.model.tps.image

import com.google.gson.annotations.SerializedName

data class ImageData(
    @SerializedName("image")
    var image: MutableList<Image>
)