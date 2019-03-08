package com.pantaubersama.app.data.model.debat

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Condition
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Progress
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Role
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Status
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Type
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_AUDIENCE_ITEM
import java.io.Serializable

data class ChallengeResponse(
    @SerializedName("data")
    val challengeData: ChallengeData
)

data class ChallengeItemResponse(
    @SerializedName("data")
    val challengeItemData: ChallengeItemData
)

data class ChallengeData(
    @SerializedName("challenges")
    val challenges: List<Challenge>
)

data class ChallengeItemData(
    @SerializedName("challenge")
    val challenge: Challenge
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
    var progress: String,
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
) : ItemModel, Serializable {

    override fun getType(): Int = PantauConstants.ItemModel.TYPE_CHALLENGE_ITEM

    val status: String
        get() = when {
            condition == Condition.EXPIRED -> Status.EXPIRED
            progress == Progress.LIVE_NOW -> Status.LIVE_NOW
            progress == Progress.COMING_SOON -> Status.COMING_SOON
            progress == Progress.DONE -> Status.DONE
            type == Type.OPEN_CHALLENGE -> Status.OPEN_CHALLENGE
            type == Type.DIRECT_CHALLENGE -> {
                if (condition == Condition.REJECTED) Status.DENIED
                else Status.DIRECT_CHALLENGE
            }
            else -> throw ErrorException("Status challenge tidak diketahui")
        }

    val challenger: Audience
        get() = audiences.find { it.role == Role.CHALLENGER }
            ?: throw ErrorException("Tidak ada penantang")

    var opponent: Audience? = null
        get() = audiences.find { it.role == Role.OPPONENT }

    val opponentCandidates: List<Audience>
        get() = audiences.filter { it.role == Role.OPPONENT_CANDIDATE }
}

data class Audience(
    @SerializedName("about")
    val about: String?,
    @SerializedName("avatar")
    val avatar: Image?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("user_id")
    val userId: Any?,
    @SerializedName("username")
    val username: String
) : Serializable, ItemModel {
    override fun getType(): Int = TYPE_AUDIENCE_ITEM
}