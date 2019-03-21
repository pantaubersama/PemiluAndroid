package com.pantaubersama.app.data.model.tps.realcount.realcountoutput

import com.google.gson.annotations.SerializedName

data class RealCountResponse(
    @SerializedName("data")
    var data: RealCountData
)