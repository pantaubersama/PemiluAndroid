package com.pantaubersama.app.data.model.linimasa

import com.google.gson.annotations.SerializedName

data class FeedsResponse(
    @SerializedName("data") var feeds: Feeds? = null
)