package com.pantaubersama.app.data.model.tps.candidate

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.Dapil
import java.io.Serializable

data class Candidate(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("serial_number")
    var serialNumber: Int,
    @SerializedName("actor_type")
    var actorType: String,
    @SerializedName("dapil")
    var dapil: Dapil,
    @SerializedName("political_party")
    var politicalParty: PoliticalParty,
    var candidateCount: Long
) : ItemModel, Serializable {
    override fun getType(): Int {
        return 1
    }
}