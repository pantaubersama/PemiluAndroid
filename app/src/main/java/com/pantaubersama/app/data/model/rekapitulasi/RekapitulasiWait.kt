package com.pantaubersama.app.data.model.rekapitulasi

import com.pantaubersama.app.data.model.ItemModel

data class RekapitulasiWait(
    var isReady: Boolean = false
) : ItemModel {
    override fun getType(): Int {
        return 0
    }
}