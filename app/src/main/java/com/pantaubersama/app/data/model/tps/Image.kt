package com.pantaubersama.app.data.model.tps

import com.pantaubersama.app.data.model.ItemModel
import java.io.File

data class Image(
    var file: File
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}