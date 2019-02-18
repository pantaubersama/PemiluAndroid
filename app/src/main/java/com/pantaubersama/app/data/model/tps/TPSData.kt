package com.pantaubersama.app.data.model.tps

import com.pantaubersama.app.data.model.ItemModel

data class TPSData(
    var name: String,
    var province: String,
    var district: String,
    var subDistrict: String,
    var village: String,
    var status: Int
) : ItemModel {
    override fun getType(): Int {
        return 14
    }
}