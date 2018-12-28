package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Avatar(
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: Thumbnail? = null,
    @SerializedName("thumbnail_square")
    @Expose
    var thumbnailSquare: ThumbnailSquare? = null,
    @SerializedName("medium")
    @Expose
    var medium: Medium? = null,
    @SerializedName("medium_square")
    @Expose
    var mediumSquare: MediumSquare? = null
) : Serializable