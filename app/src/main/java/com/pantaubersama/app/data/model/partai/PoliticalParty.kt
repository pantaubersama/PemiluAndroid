package com.pantaubersama.app.data.model.partai

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.kandidat.CandidateData
import com.pantaubersama.app.data.model.meta.Meta
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

data class PoliticalPartiesResponse(
    @SerializedName("data") val data: PoliticalPartiesData
) : Serializable

data class PoliticalPartiesData(
    @SerializedName("political_parties") val politicalParties: MutableList<PoliticalParty>,
    @SerializedName("meta") val meta: Meta
) : Serializable

data class PoliticalParty(
    @SerializedName("id")
    var id: String,
    @SerializedName("image")
    var image: Image?,
    @SerializedName("name")
    var name: String,
    @SerializedName("number")
    var number: Int,
    // dummy object
    var members: MutableList<CandidateData>?
) : ItemModel, Serializable {
    override fun getType(): Int {
        return PantauConstants.ItemModel.TYPE_PARTAI
    }
}
