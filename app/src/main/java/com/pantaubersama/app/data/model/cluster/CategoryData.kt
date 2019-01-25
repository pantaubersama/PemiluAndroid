package com.pantaubersama.app.data.model.cluster

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta

data class CategoryData(
    @SerializedName("data")
    @Expose
    var data: Data
)

data class Data(
    @SerializedName("categories")
    @Expose
    var categories: MutableList<Category>,
    @SerializedName("category")
    @Expose
    var category: Category,
    @SerializedName("meta")
    @Expose
    val meta: Meta
)