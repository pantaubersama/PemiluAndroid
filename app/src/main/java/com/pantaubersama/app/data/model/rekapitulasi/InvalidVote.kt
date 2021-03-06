package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InvalidVote(
    @SerializedName("total_vote")
    var total: Int,
    @SerializedName("percentage")
    var percentage: Double
) : Serializable