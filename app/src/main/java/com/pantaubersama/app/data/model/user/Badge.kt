package com.pantaubersama.app.data.model.user
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.meta.Meta
import java.io.Serializable

data class BadgeResponse(
    @SerializedName("data") val data: BadgeData
) : Serializable

data class BadgeData(
    @SerializedName("achieved_badges") val achievedBadges: List<Badge>,
    @SerializedName("badges") val badges: List<Badge>,
    @SerializedName("meta") val meta: Meta
) : Serializable

data class Badge(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: Image,
    @SerializedName("position") val position: Int
) : Serializable {
    var achieved: Boolean = false
}