package com.pantaubersama.app.data.model.janjipolitik

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants

/**
 * @author edityomurti on 25/12/2018 21:44
 */
data class JanjiPolitik(
    var id: String? = null,
    var title: String? = null,
    var content: String? = null
) : ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_JANPOL
}