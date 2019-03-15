package com.pantaubersama.app.data.model.tps.candidate

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import java.io.Serializable

data class CandidateData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("serial_number")
    var serialNumber: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("acronym")
    var acronym: String,
    @SerializedName("logo")
    var logo: String,
    @SerializedName("actor_type")
    var actorType: String,
    @SerializedName("candidates")
    var candidates: MutableList<Candidate>,
    var partyCount: Int,
    var allCandidateCount: Int,
    var totalCount: Int
) : ItemModel, Serializable {

    override fun getType(): Int {
        return 1
    }
}