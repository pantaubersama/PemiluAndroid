package com.pantaubersama.app.data.model.bannerinfo

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.image.Image
import java.io.Serializable

/**
 * @author edityomurti on 27/12/2018 19:49
 */
data class BannerInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("page_name") var pageName: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("header_image") var headerImage: Image? = null,
    @SerializedName("image") var image: Image? = null
) : ItemModel, Serializable {

    override fun getType(): Int = 666
}