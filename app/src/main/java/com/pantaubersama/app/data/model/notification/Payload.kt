package com.pantaubersama.app.data.model.notification

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationData(
    @SerializedName("title") val title: String? = "",
    @SerializedName("body") val body: String? = ""
) {
    companion object {
        val TAG = "notification"
    }
}

data class PemiluBroadcast(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("event_type") val eventType: String = "",
    @SerializedName("link") val link: String = ""
) {
    companion object {
        val TAG = "pemilu_broadcast"
    }
}

data class QuestionNotif(
    @SerializedName("id") val id: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("body") val body: String = ""
) {
    companion object {
        val TAG = "question"
    }
}

data class AchievedBadgeNotif(
    @SerializedName("achieved_id") val achievedId: String,
    @SerializedName("badge") val badge: BadgeNotif
) : Serializable {
    companion object {
        const val TAG = "achieved_badge"
    }
}

data class BadgeNotif(
    @SerializedName("image") val image: ImageNotif,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("namespace") val namespace: String,
    @SerializedName("id") val id: String,
    @SerializedName("position") val position: String,
    @SerializedName("image_gray") val imageGray: ImageNotif
) : Serializable

data class ImageNotif(
    @SerializedName("thumbnail") val thumbnail: String = "",
    @SerializedName("thumbnail_square") val thumbnailSquare: String = "",
    @SerializedName("large") val large: String = "",
    @SerializedName("large_square") val largeSquare: String = ""
) : Serializable

data class QuizNotif(
    @SerializedName("image") val image: ImageNotif,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("quiz_questions_count") val quizQuestionsCount: Int
) : Serializable {
    companion object {
        const val TAG = "quiz"
    }
}