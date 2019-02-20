package com.pantaubersama.app.data.model.wordstadium

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

data class BidangKajian(
        var id: String? = null,
        @SerializedName("bidang_kajian")
        var bidangKajian: String? = null
) : Serializable, ItemModel {
    override fun getType(): Int {
        return PantauConstants.ItemModel.TYPE_BIDANG_KAJIAN
    }
}