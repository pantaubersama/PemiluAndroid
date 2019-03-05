package com.pantaubersama.app.data.model.tps

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "province")
data class Province(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("code")
    @Expose
    var code: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("level")
    @Expose
    var level: Int,
    @SerializedName("domain_name")
    @Expose
    var domainName: String,
    @SerializedName("id_wilayah")
    @Expose
    var idWilayah: Int
)