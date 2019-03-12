package com.pantaubersama.app.data.model.debat

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_OPPONENT
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_OPPONENT
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Role
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_AUDIENCE

/**
 * @author edityomurti on 12/02/2019 14:09
 */

data class WordListResponse(
    @SerializedName("data")
    val wordListData: WordListData
)

data class WordItemResponse(
    @SerializedName("data")
    val wordItemData: WordItemData
)

data class WordListData(
    @SerializedName("words")
    val wordList: MutableList<WordItem>
)

data class WordItemData(
    @SerializedName("word")
    val word: WordItem
)

data class WordItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("challenge_id")
    val challengeId: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("read_time")
    val readTime: Float,
    @SerializedName("time_spent")
    val timeSpent: Float,
    @SerializedName("time_left")
    val timeLeft: Float,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("author")
    val author: Audience,
    var isClaped: Boolean = false,
    var clapCount: Int = 0
) : ItemModel {
    companion object {
        const val TAG = "word"
    }

    override fun getType(): Int = when {
        author.role == Role.CHALLENGER -> WORD_TYPE_CHALLENGER
        author.role == Role.OPPONENT -> WORD_TYPE_OPPONENT
        author.role == Role.AUDIENCE || type == WordConstants.Type.COMMENT -> WORD_TYPE_AUDIENCE
        else -> throw ErrorException("Role word tidak sesuai")
    }
}

data class WordInputItem(
    val role: String,
    var isActive: Boolean = false,
    var body: String = ""
) : ItemModel {
    override fun getType(): Int = when (role) {
        Role.CHALLENGER -> WORD_INPUT_CHALLENGER
        Role.OPPONENT -> WORD_INPUT_OPPONENT
        else -> -1
    }
}

object WordConstants {
    object Type {
        const val ATTACK = "Attack"
        const val COMMENT = "Comment"
    }
}