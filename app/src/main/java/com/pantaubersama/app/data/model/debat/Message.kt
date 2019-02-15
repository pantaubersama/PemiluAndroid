package com.pantaubersama.app.data.model.debat

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE

/**
 * @author edityomurti on 12/02/2019 14:09
 */

data class Message(
    val id: String,
    val body: String,
    val sender: User
): ItemModel {
    override fun getType(): Int {
        return MESSAGE_TYPE_LEFT_SIDE
    }
}