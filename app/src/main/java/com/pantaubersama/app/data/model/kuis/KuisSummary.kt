package com.pantaubersama.app.data.model.kuis
import com.google.gson.annotations.SerializedName

data class KuisSummaryResponse(
    @SerializedName("data") val data: KuisSummaryData
)

data class KuisSummaryData(
    @SerializedName("questions") val questions: List<AnsweredQuestion>
)

data class AnsweredQuestion(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String,
    @SerializedName("answers") val answers: List<TeamAnswer>,
    @SerializedName("answered") val answered: TeamAnswer
)

data class TeamAnswer(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String,
    @SerializedName("team") val team: Team
)
