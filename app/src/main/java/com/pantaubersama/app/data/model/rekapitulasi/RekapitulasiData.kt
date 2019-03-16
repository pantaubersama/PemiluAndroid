package com.pantaubersama.app.data.model.rekapitulasi

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.kuis.TeamPercentage

data class RekapitulasiData(
    var totalParticipant: Long,
    var updatedAt: String,
    var teams: List<TeamPercentage>,
    var location: String
) : ItemModel {
    override fun getType(): Int {
        return 15
    }
}