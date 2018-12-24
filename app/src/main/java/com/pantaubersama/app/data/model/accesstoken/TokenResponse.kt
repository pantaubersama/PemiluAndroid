package com.pantaubersama.app.data.model.accesstoken

import com.google.gson.annotations.SerializedName

/**
 * Created by ali on 19/12/17.
 */
data class TokenResponse(
    @SerializedName("data") var token: Token?
)