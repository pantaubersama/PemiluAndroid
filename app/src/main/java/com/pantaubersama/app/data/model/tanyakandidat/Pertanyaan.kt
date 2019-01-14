package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

class Pertanyaan(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("body")
    @Expose
    var body: String? = null,
    @SerializedName("created_at_in_word")
    @Expose
    var createdAtInWord: CreatedAtInWord? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,
    @SerializedName("like_count")
    @Expose
    var likeCount: Int? = null,
    @SerializedName("user")
    @Expose
    var user: User? = null,
    @SerializedName("is_liked")
    @Expose
    var isliked: Boolean? = false,
    var viewType: Int? = null
) : Serializable, ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_PERTANYAAN
}