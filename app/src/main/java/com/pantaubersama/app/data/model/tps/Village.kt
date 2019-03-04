package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Village(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("code")
    @Expose
    var code: Int,
    @SerializedName("district_code")
    @Expose
    var districtCode: Int,
    @SerializedName("name")
    @Expose
    var name: String
)