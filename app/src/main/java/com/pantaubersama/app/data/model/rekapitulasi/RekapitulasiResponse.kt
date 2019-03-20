package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName

data class RekapitulasiResponse(
    @SerializedName("data")
    var data: RekapitulasiData
)