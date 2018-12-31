package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName

/**
 * @author edityomurti on 19/12/2018 14:20
 */
data class FeedsItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("team") var team: Team? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("source") var source: Source? = null,
    @SerializedName("account") var account: Account? = null
)