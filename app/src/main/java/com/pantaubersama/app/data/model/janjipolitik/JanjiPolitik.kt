package com.pantaubersama.app.data.model.janjipolitik

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.meta.Meta
import com.pantaubersama.app.utils.PantauConstants

/**
 * @author edityomurti on 25/12/2018 21:44
 */
data class JanjiPolitik(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("image") var image: Image? = null,
    @SerializedName("creator") var creator: Creator? = null
    ) : ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_JANPOL
}

data class JanjiPolitikResponse(
    @SerializedName("data") var data: JanjiPolitikData?
)

data class JanjiPolitikData(
    @SerializedName("janji_politiks") var janjiPolitikList: MutableList<JanjiPolitik>?,
    @SerializedName("meta") var meta: Meta?
)

data class Creator(
    @SerializedName("id") var id: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("full_name") var fullName: String?,
    @SerializedName("about") var about: String?,
    @SerializedName("avatar") var avatar: Image?,
    @SerializedName("cluster") var cluster: ClusterItem?
)