package com.pantaubersama.app.data.model.user
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta

data class BadgeResponse(
    @SerializedName("data") val data: BadgeData
)

data class BadgeData(
    @SerializedName("achieved_badges") val achievedBadges: List<Badge>,
    @SerializedName("badges") val badges: List<Badge>,
    @SerializedName("meta") val meta: Meta
)

data class Badge(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: Image,
    @SerializedName("position") val position: Int
) {
    var achieved: Boolean = false
}

data class Image(
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail") val thumbnail: Thumbnail,
    @SerializedName("thumbnail_square") val thumbnailSquare: ThumbnailSquare
)