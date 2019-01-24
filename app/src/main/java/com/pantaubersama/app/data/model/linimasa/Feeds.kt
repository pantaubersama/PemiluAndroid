package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta

data class Feeds(
    @SerializedName("feeds") var feedList: MutableList<FeedsItem>? = null,
    @SerializedName("meta") var meta: Meta? = null
)