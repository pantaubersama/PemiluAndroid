package com.pantaubersama.app.data.model.urlpreview

import com.google.gson.annotations.SerializedName

/**
 * @author edityomurti on 20/03/2019 13:53
 */
data class UrlItemResponse(
    @SerializedName("data")
    val data: UrlItemData
)

data class UrlItemData(
    @SerializedName("url")
    val url: UrlItem
)

data class UrlItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("source_url")
    val sourceUrl: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("best_title")
    val bestTitle: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("best_description")
    val bestDescriptiom: String?,
    @SerializedName("best_image")
    val bestImage: String?
)