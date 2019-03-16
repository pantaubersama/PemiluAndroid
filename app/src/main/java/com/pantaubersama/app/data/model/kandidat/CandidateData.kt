package com.pantaubersama.app.data.model.kandidat

import com.pantaubersama.app.data.model.ItemModel

data class CandidateData(
    var name: String,
    var number: Int
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}