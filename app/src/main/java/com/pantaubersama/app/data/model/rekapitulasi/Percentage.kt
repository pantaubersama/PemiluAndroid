package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.realcount.Candidate
import java.io.Serializable

data class Percentage(
    @SerializedName("summary_type")
    var summaryType: String,
    @SerializedName("candidates")
    var candidates: List<Candidate>?,
    @SerializedName("invalid_vote")
    var invalidVote: InvalidVote?,
    @SerializedName("total_vote")
    var totalVote: Int,
    @SerializedName("region")
    var region: Region
) : ItemModel, Serializable {
    override fun getType(): Int {
        return 0
    }
}