package com.pantaubersama.app.data.model.tps.image

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    var id: String,
    @SerializedName("file")
    var file: File,
    @SerializedName("hitung_real_count_id")
    var hitungRealCountId: String,
    @SerializedName("image_type")
    var imageType: String
)