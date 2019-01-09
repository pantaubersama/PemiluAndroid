package com.pantaubersama.app.data.model.kuis
import com.google.gson.annotations.SerializedName

data class KuisResultResponse(
    @SerializedName("data") val data: KuisResultData
)

data class KuisResultData(
    @SerializedName("teams") val teams: List<TeamPercentage>,
    @SerializedName("answers") val answers: List<Int>
)