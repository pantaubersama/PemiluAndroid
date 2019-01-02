package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.createdat.CreatedAt
import com.pantaubersama.app.data.model.user.User
import java.io.Serializable

class Pertanyaan(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("body")
    @Expose
    var body: String? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: CreatedAt? = null,
    @SerializedName("created")
    @Expose
    var created: String? = null,
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
) : Serializable