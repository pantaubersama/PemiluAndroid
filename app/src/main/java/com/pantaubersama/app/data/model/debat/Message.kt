package com.pantaubersama.app.data.model.debat

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE

/**
 * @author edityomurti on 12/02/2019 14:09
 */

data class Message(
    val id: String,
    val body: String,
    val sender: Profile,
    var isLiked: Boolean,
    var likedCount: Int,
    var createdAt: Long
) : ItemModel {
    override fun getType(): Int {
        return MESSAGE_TYPE_LEFT_SIDE
    }
}