package com.pantaubersama.app.data.model.tps.realcount.realcountoutput

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.tps.realcount.RealCount

data class RealCountData(
    @SerializedName("calculation")
    var calculation: RealCount
)