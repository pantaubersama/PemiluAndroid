package com.pantaubersama.app.data.model.tps.candidate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PoliticalParty(
    @SerializedName("id")
    var id: Int,
    @SerializedName("serial_number")
    var serialNumber: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("acronym")
    var acronym: String,
    @SerializedName("logo")
    var logo: String
) : Serializable