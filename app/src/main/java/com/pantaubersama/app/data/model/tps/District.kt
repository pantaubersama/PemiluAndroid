package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class District(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("code")
    @Expose
    var code: Int,
    @SerializedName("regency_code")
    @Expose
    var regencyCode: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("id_parent")
    @Expose
    var idParent: Int,
    @SerializedName("id_wilayah")
    @Expose
    var idWilayah: Int,
    @SerializedName("level")
    @Expose
    var level: Int
) : Serializable