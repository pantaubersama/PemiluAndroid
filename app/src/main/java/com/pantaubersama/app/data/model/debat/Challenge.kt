package com.pantaubersama.app.data.model.debat

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.remote.exception.ErrorException
import java.io.Serializable

data class ChallengeResponse(
    @SerializedName("data")
    val challengeData: ChallengeData
)

data class ChallengeData(
    @SerializedName("challenges")
    val challenges: List<Challenge>
)

data class Challenge(
    @SerializedName("audiences")
    val audiences: List<Audience>,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("progress")
    val progress: String,
    @SerializedName("show_time_at")
    val showTimeAt: String,
    @SerializedName("statement")
    val statement: String,
    @SerializedName("statement_source")
    val statementSource: String,
    @SerializedName("time_limit")
    val timeLimit: Int,
    @SerializedName("topic_list")
    val topicList: List<String>,
    @SerializedName("type")
    val type: String
) {

    val status: DebatItem.Challenge.Status
        get() = when {
            condition == ChallengeConstants.CONDITION_EXPIRED -> DebatItem.Challenge.Status.EXPIRED
            condition == ChallengeConstants.CONDITION_REJECTED -> DebatItem.Challenge.Status.DENIED
            // condition == ChallengeConstants.CONDITION_ONGOING
            type == ChallengeConstants.TYPE_OPEN_CHALLENGE -> DebatItem.Challenge.Status.OPEN
            type == ChallengeConstants.TYPE_DIRECT_CHALLENGE -> DebatItem.Challenge.Status.DIRECT
            else -> throw ErrorException("Status challenge tidak diketahui")
        }

    fun getChallenger(): Audience {
        return audiences.find { it.role == ChallengeConstants.ROLE_CHALLENGER }
            ?: throw ErrorException("Tidak ada penantang")
    }

    fun getOpponent(): Audience? {
        return audiences.find { it.role == ChallengeConstants.ROLE_OPPONENT }
    }

    fun getOpponentCandidates(): List<Audience> {
        return audiences.filter { it.role == ChallengeConstants.ROLE_OPPONENT_CANDIDATE }
    }
}

data class Audience(
    @SerializedName("about")
    val about: String,
    @SerializedName("avatar")
    val avatar: Image,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("user_id")
    val userId: Any?,
    @SerializedName("username")
    val username: String
): Serializable

// TODO: remove
val DUMMY_CHALLENGER = Audience("", Image(), "", "Ratu CebonganYK", "",
    ChallengeConstants.ROLE_CHALLENGER, null, "@ratu_cebonganYK")
val DUMMY_OPPONENT = Audience("", Image(), "", "Raja Kampreta", "",
    ChallengeConstants.ROLE_CHALLENGER, null, "@raja_kampreta")