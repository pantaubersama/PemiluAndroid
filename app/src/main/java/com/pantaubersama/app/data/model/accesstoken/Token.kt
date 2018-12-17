package com.pantaubersama.app.data.model.accesstoken

import com.google.gson.annotations.SerializedName

/**
 * Created by ali on 19/12/17.
 */
data class Token(
    @SerializedName("access_token") var accessToken: String?,
    @SerializedName("refresh_token") var refreshToken: String?,
    @SerializedName("token_type") var tokenType: String?
)