package com.pantaubersama.app.data.model.debat

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.User

/**
 * @author edityomurti on 12/02/2019 14:09
 */

data class Message(
    val id: String,
    val body: String,
    val sender: User
): ItemModel {
    override fun getType(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}