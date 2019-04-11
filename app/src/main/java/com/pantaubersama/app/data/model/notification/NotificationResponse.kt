package com.pantaubersama.app.data.model.notification

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel

data class NotificationResponse(
    @SerializedName("data")
    val data: NotificationWrapper
)

data class NotificationWrapper(
    @SerializedName("notifications")
    val notifications: MutableList<NotificationWhole>
)

data class NotificationWhole(
    @SerializedName("data")
    val data: DataPayload,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_action")
    val is_action: Boolean,
    @SerializedName("notification")
    val notification: Notification,
    @SerializedName("pemilu_broadcast")
    var broadcast: PemiluBroadcast?
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}

data class DataPayload(
    @SerializedName("event_type")
    val event_type: String,
    @SerializedName("notif_type")
    val notif_type: String?,
    @SerializedName("payload")
    val payload: Payload
)

data class Payload(
    @SerializedName("event_type")
    val event_type: String,
    @SerializedName("notif_type")
    val notif_type: String?,
    @SerializedName("notification")
    val notification: Notification,
    @SerializedName("question")
    val question: Question,
    @SerializedName("achieved_badge")
    val badgeNotif: AchievedBadge,
    @SerializedName("quiz")
    var quizNotif: Quiz?,
    @SerializedName("pemilu_broadcast")
    var broadcast: PemiluBroadcast?
)

data class Notification(
    @SerializedName("body")
    val body: String,
    @SerializedName("title")
    val title: String
)

data class Quiz(
    @SerializedName("image") val image: ImageNotif,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("quiz_questions_count") val quizQuestionsCount: Int
) : ItemModel {
    override fun getType(): Int {
        return 3
    }

    companion object {
        const val TAG = "quiz"
    }
}

data class Question(
    @SerializedName("id") val id: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("body") val body: String = "",
    @SerializedName("upvote_by") val upvotedBy: UpvotedBy
) : ItemModel {
    override fun getType(): Int {
        return 2
    }

    companion object {
        val TAG = "question"
    }
}

data class AchievedBadge(
    @SerializedName("achieved_id") val achievedId: String,
    @SerializedName("badge") val badge: BadgeNotif
) : ItemModel {

    override fun getType(): Int {
        return 1
    }

    companion object {
        const val TAG = "achieved_badge"
    }
}

data class Badge(
    @SerializedName("image") val image: ImageNotif,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("namespace") val namespace: String,
    @SerializedName("id") val id: String,
    @SerializedName("position") val position: String,
    @SerializedName("image_gray") val imageGray: ImageNotif
)