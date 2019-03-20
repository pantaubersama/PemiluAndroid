package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName

data class InvalidVote(
    @SerializedName("total")
    var total: Int,
    @SerializedName("percentage")
    var percentage: Double
)