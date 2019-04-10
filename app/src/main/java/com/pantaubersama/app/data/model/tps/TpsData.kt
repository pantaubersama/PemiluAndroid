package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta

data class TpsData(
    @SerializedName("real_counts")
    @Expose
    var tpses: MutableList<TPS>,
    @SerializedName("real_count")
    @Expose
    var tps: TPS,
    @SerializedName("meta")
    @Expose
    var meta: Meta
)