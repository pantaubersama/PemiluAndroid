package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("avatar") var avatar: String? = null
)