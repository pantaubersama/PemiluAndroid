package com.pantaubersama.app.data.model.cluster

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.meta.Meta
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

/**
 * @author edityomurti on 27/12/2018 00:11
 */

data class ClusterItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("is_eligible") var isEligible: Boolean? = false,
    @SerializedName("image") var image: Image? = Image(),
    @SerializedName("is_displayed") var isDisplayed: Boolean? = true,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("category") var category: Category? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("members_count") var memberCount: Int? = null,
    @SerializedName("magic_link") var magicLink: String? = null,
    @SerializedName("is_link_active") var isLinkActive: Boolean? = false
) : Serializable, ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_CLUSTER_ITEM
}

data class ClustersResponse(
    @SerializedName("data") var clustersData: ClustersData,
    @SerializedName("meta") var meta: Meta
)

data class ClustersData(
    @SerializedName("clusters") var clusterList: MutableList<ClusterItem>
)

data class Category(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String
) : Serializable