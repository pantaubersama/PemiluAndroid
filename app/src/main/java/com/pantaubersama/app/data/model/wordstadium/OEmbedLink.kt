package com.pantaubersama.app.data.model.wordstadium

import com.google.gson.annotations.SerializedName

data class OEmbedLink(
        @SerializedName("author_name")
        var authorName: String?,
        @SerializedName("author_url")
        var authorUrl: String?,
        @SerializedName("cache_age")
        var cacheAge: String?,
        @SerializedName("height")
        var height: Any?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("provider_name")
        var providerName: String?,
        @SerializedName("provider_url")
        var providerUrl: String?,
        @SerializedName("type")
        var type: String?,
        @SerializedName("url")
        var url: String?,
        @SerializedName("version")
        var version: String?,
        @SerializedName("width")
        var width: Int?
)