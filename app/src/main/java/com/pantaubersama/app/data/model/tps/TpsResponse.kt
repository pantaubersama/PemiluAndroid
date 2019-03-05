package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TpsResponse(
    @SerializedName("data")
    @Expose
    var tpsData: TpsData
)