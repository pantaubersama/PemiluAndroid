package com.pantaubersama.app.data.model.tps.image

import com.pantaubersama.app.data.model.ItemModel

data class Image(
    var uri: String
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}