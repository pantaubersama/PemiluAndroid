package com.pantaubersama.app.data.model.tps.candidate

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.image.Image
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
    var logo: Image,
    @SerializedName("actor_type")
    var actorType: String,
    @SerializedName("candidates")
    var candidates: MutableList<Candidate>,
    var partyCount: Long,
    var allCandidateCount: Long,
    var totalCount: Long
) : ItemModel, Serializable {

    override fun getType(): Int {
        return 1
    }
}