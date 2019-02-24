package com.pantaubersama.app.data.model.debat

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_INPUT_LEFT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_INPUT_RIGHT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_RIGHT_SIDE

/**
 * @author edityomurti on 12/02/2019 14:09
 */

val MESSAGE_INPUT_LEFT = InputMessageItem(InputMessageItem.Type.INPUT_LEFT_SIDE, "")
val MESSAGE_INPUT_RIGHT = InputMessageItem(InputMessageItem.Type.INPUT_RIGHT_SIDE, "")

data class MessageItem(
    val id: String,
    var body: String,
    val sender: Profile,
    var isLiked: Boolean,
    var likedCount: Int,
    var createdAt: Long,
    val type: Type
) : ItemModel {
    override fun getType(): Int = when (type) {
        Type.LEFT_SIDE -> MESSAGE_TYPE_LEFT_SIDE
        Type.RIGHT_SIDE -> MESSAGE_TYPE_RIGHT_SIDE
    }

    enum class Type {
        LEFT_SIDE, RIGHT_SIDE
    }
}

data class InputMessageItem(
    val type: Type,
    var body: String,
    var inputState: InputState? = InputState.ACTIVE
): ItemModel {
    override fun getType(): Int = when (type) {
        Type.INPUT_LEFT_SIDE -> MESSAGE_INPUT_LEFT_SIDE
        Type.INPUT_RIGHT_SIDE -> MESSAGE_INPUT_RIGHT_SIDE
    }

    enum class Type {
        INPUT_LEFT_SIDE, INPUT_RIGHT_SIDE
    }

    enum class InputState {
        ACTIVE, INACTIVE
    }
}