package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.meta.Meta
import java.io.Serializable

data class BadgeResponse(
    @SerializedName("data") val data: BadgeData
) : Serializable

data class AchievedBadgeResponse(
    @SerializedName("data") val data: AchievedBadgeData
)

data class BadgeData(
    @SerializedName("achieved_badges") val achievedBadges: List<AchievedBadge>,
    @SerializedName("badges") val badges: List<Badge>,
    @SerializedName("meta") val meta: Meta
) : Serializable

data class AchievedBadgeData(
    @SerializedName("achieved_badge") val achievedBadge: AchievedBadge
)

data class AchievedBadge(
    @SerializedName("achieved_id") val achievedId: String,
    @SerializedName("badge") val badge: Badge,
    @SerializedName("user") val user: User
) : Serializable

data class Badge(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: Image,
    @SerializedName("position") val position: Int
) : Serializable {
    var achievedId: String? = null
    var achieved: Boolean = false
}