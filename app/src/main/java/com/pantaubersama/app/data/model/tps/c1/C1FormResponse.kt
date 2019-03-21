package com.pantaubersama.app.data.model.tps.c1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class C1FormResponse(
    @SerializedName("data")
    @Expose
    var data: C1FormData
)

data class C1FormData(
    @SerializedName("form_c1")
    @Expose
    var c1Form: C1Form
)