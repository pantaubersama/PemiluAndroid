package com.pantaubersama.app.data.model.capres

import androidx.annotation.DrawableRes
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

data class PaslonData(
    var paslonNumber: Int,
    var paslonName: String,
    @DrawableRes var paslonImage: Int? = null,
    var president: String? = null,
    var vicePresident: String? = null
) : ItemModel, Serializable {
    override fun getType(): Int {
        return PantauConstants.ItemModel.TYPE_PASLON
    }
}