package com.pantaubersama.app.data.model.debat

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_KOMENTAR_ITEM

/**
 * @author edityomurti on 20/02/2019 13:02
 */
data class Komentar(
    val id: String,
    val content: String,
    val createdAt: String,
    val user: Profile
) : ItemModel {
    override fun getType(): Int = TYPE_KOMENTAR_ITEM
}