package com.pantaubersama.app.data.model.kuis

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import com.google.gson.annotations.SerializedName

data class KuisUserResult(val percentage: Int, val team: Team, val meta: Quizzes) : ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_KUIS_RESULT
}

data class KuisUserResultResponse(
    @SerializedName("data") val data: KuisUserResultData
)

data class KuisUserResultData(
    @SerializedName("teams") val teams: List<Teams>,
    @SerializedName("meta") val meta: KuisMeta
)

data class KuisMeta(
    @SerializedName("quizzes") val quizzes: Quizzes
)

data class Quizzes(
    @SerializedName("total") val total: Int,
    @SerializedName("finished") val finished: Int
)

data class Teams(
    @SerializedName("team") val team: Team,
    @SerializedName("percentage") val percentage: Int
)

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("avatar") val avatar: String
)