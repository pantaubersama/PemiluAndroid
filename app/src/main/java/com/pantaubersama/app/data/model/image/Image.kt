package com.pantaubersama.app.data.model.image

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import java.io.Serializable

data class Image(
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
    var mediumSquare: MediumSquare? = null,
    @SerializedName("large") var large: Large? = null
) : Serializable

data class Large(
    @SerializedName("url") var url: String? = null
) : Serializable