package com.pantaubersama.app.data.model.kuis
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.utils.PantauConstants

data class KuisResult(val percentage: Float, val team: Team, val user: User, val quizParticipation: QuizParticipation, val title: String) : ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_KUIS_RESULT
}

data class KuisResultResponse(
    @SerializedName("data") val data: KuisResultData
)

data class KuisResultData(
    @SerializedName("teams") val teams: List<TeamPercentage>,
    @SerializedName("answers") val answers: List<Int>,
    @SerializedName("quiz_participation") val quizParticipation: QuizParticipation,
    @SerializedName("user") val user: User,
    @SerializedName("quiz") val quiz: KuisItem
)