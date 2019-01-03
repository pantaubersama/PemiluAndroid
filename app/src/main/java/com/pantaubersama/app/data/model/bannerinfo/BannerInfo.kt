package com.pantaubersama.app.data.model.bannerinfo

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel

/**
 * @author edityomurti on 27/12/2018 19:49
 */
data class BannerInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("page_name") var pageName: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("header_image") var headerImage: HeaderImage? = null,
    @SerializedName("image") var image: Image? = null
): ItemModel {

    data class HeaderImage(
        @SerializedName("url") var url: String?,
        @SerializedName("large") var large: Large? = null
    )

    data class Image(
        @SerializedName("url") var url: String?,
        @SerializedName("large") var large: Large? = null
    )

    data class Large(
        @SerializedName("url") var url: String? = null
    )

    override fun getType(): Int = 666
}