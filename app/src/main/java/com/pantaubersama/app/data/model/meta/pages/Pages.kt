package com.pantaubersama.app.data.model.meta.pages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pages(
    @SerializedName("total")
    @Expose
    var total: Int,
    @SerializedName("per_page")
    @Expose
    var perPage: Int,
    @SerializedName("page")
    @Expose
    var page: Int
) : Serializable