package com.pantaubersama.app.data.model.kuis

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.user.User

data class KuisUserResult(val percentage: Float, val team: Team, val meta: Quizzes, val user: User) : ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_KUIS_RESULT
}

data class KuisUserResultResponse(
    @SerializedName("data") val data: KuisUserResultData
)

data class KuisUserResultData(
    @SerializedName("teams") val teams: List<TeamPercentage>,
    @SerializedName("meta") val meta: KuisMeta,
    @SerializedName("user") val user: User
)

data class KuisMeta(
    @SerializedName("quizzes") val quizzes: Quizzes
)

data class Quizzes(
    @SerializedName("total") val total: Int,
    @SerializedName("finished") val finished: Int
)

data class TeamPercentage(
    @SerializedName("team") val team: Team,
    @SerializedName("percentage") val percentage: Float
)

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("avatar") val avatar: String
)