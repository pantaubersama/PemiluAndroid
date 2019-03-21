package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel

data class Rekapitulasi(
    @SerializedName("percentage")
    var percentages: Percentage?,
    @SerializedName("region")
    var region: Region
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}