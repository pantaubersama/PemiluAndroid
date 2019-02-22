package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

class Pertanyaan(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("body")
    @Expose
    var body: String,
    @SerializedName("created_at_in_word")
    @Expose
    var createdAtInWord: CreatedAtInWord? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("like_count")
    @Expose
    var likeCount: Int,
    @SerializedName("user")
    @Expose
    var user: Profile,
    @SerializedName("is_liked")
    @Expose
    var isliked: Boolean = false,
    var viewType: Int
) : Serializable, ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_PERTANYAAN
}