package com.pantaubersama.app.data.model.partai

import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("medium")
        var medium: Medium?,
        @SerializedName("medium_square")
        var mediumSquare: MediumSquare?,
        @SerializedName("thumbnail")
        var thumbnail: Thumbnail?,
        @SerializedName("thumbnail_square")
        var thumbnailSquare: ThumbnailSquare?,
        @SerializedName("url")
        var url: String?
)