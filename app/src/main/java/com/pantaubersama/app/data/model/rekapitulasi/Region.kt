package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Region(
    @SerializedName("id")
    var id: Int,
    @SerializedName("code")
    var code: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("level")
    var level: Int,
    @SerializedName("domain_name")
    var domainName: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("id_wilayah")
    var idWilayah: Int
) : Serializable