package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Regency(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("province_id")
    @Expose
    var provinceId: Int,
    @SerializedName("code")
    @Expose
    var code: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("domain_name")
    @Expose
    var domainName: String,
    @SerializedName("id_wilayah")
    @Expose
    var idWilayah: Int,
    @SerializedName("id_parent")
    @Expose
    var idParent: Int
) : Serializable