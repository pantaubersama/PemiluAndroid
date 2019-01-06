package com.pantaubersama.app.data.model.cluster

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.image.Image

/**
 * @author edityomurti on 27/12/2018 00:11
 */
data class ClusterItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("is_eligible") var isEligible: Boolean? = false,
    @SerializedName("image") var image: Image? = null,
    @SerializedName("is_displayed") var isDisplayed: Boolean? = true,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("member_count") var memberCount: Int? = null,
    @SerializedName("magic_link") var magicLink: String? = null,
    @SerializedName("is_link_active") var isLinkActive: Boolean? = false
)