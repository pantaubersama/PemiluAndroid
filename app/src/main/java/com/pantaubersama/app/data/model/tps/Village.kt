package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Village(
    @SerializedName("id")
    @Expose
    var id: Long,
    @SerializedName("code")
    @Expose
    var code: Long,
    @SerializedName("district_code")
    @Expose
    var districtCode: Int,
    @SerializedName("name")
    @Expose
    var name: String
) : Serializable