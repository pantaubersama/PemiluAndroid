package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta

data class TpsResponse(
    @SerializedName("real_counts")
    @Expose
    var tpses: MutableList<TPSData>,
    @SerializedName("meta")
    @Expose
    var meta: Meta
)