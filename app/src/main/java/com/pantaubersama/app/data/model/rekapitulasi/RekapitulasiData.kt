package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.user.Profile

data class RekapitulasiData(
    @SerializedName("tps")
    var tps: String?,
    @SerializedName("user")
    var user: Profile?,
    @SerializedName("percentage")
    var percentage: Percentage?,
    @SerializedName("percentages")
    var percentages: MutableList<Rekapitulasi>?
)