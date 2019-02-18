package com.pantaubersama.app.data.model.dpr

import com.pantaubersama.app.data.model.ItemModel

data class DPRData(
    var name: String,
    var number: Int
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}