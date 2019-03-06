package com.pantaubersama.app.data.model.wordstadium

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

data class LawanDebatResponse(
        @SerializedName("data") val data: LawanDebatData
) : Serializable

data class LawanDebatData(
        @SerializedName("users") val users: MutableList<LawanDebat>
) : Serializable

data class LawanDebat(
        @SerializedName("id")
        var id: String,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("profile_image_url")
        var profileImageUrl: String?,
        @SerializedName("screen_name")
        var screenName: String?,
        @SerializedName("full_name")
        var fullName: String? = null,
        @SerializedName("username")
        var username: String? = null,
        @SerializedName("avatar")
        var avatar: Avatar
) : Serializable, ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_USER
}

data class Avatar(
        @SerializedName("url")
        var url: String? = null
) : Serializable