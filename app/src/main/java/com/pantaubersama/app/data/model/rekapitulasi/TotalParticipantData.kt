package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import java.io.Serializable

data class TotalParticipantData(
    @SerializedName("total")
    var total: Int,
    @SerializedName("last_update")
    var lastUpdate: LastUpdate
) : ItemModel, Serializable {
    override fun getType(): Int {
        return 0
    }
}