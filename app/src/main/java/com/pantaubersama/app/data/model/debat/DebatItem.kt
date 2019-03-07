package com.pantaubersama.app.data.model.debat

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_CHALLENGE_ITEM
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_HEADER
import java.io.Serializable

sealed class DebatItem : ItemModel, Serializable {
    abstract val debatDetail: DebatDetail
    abstract val type: String

    override fun getType(): Int = TYPE_CHALLENGE_ITEM

    data class LiveNow(override val debatDetail: DebatDetail) : DebatItem(), Serializable {
        override val type: String = "LIVE NOW"
    }

    data class ComingSoon(
        override val debatDetail: DebatDetail,
        val date: String,
        val startEndTime: String
    ) : DebatItem(), Serializable {
        override val type: String = "COMING SOON"
    }

    data class Done(
        override val debatDetail: DebatDetail,
        val clap1: Int,
        val clap2: Int,
        val favoriteCount: Int
    ) : DebatItem(), Serializable {
        override val type: String = "DONE"
    }

    data class Challenge(
        override val debatDetail: DebatDetail,
        val opponentCandidates: Int,
        val opponentCandidateAvatar: String?,
        val status: Status
    ) : DebatItem(), Serializable {
        override val type: String = when (status) {
            Status.OPEN -> "OPEN CHALLENGE"
            Status.DIRECT -> "DIRECT CHALLENGE"
            Status.DENIED -> "DENIED"
            Status.EXPIRED -> "EXPIRED"
        }

        val isOpen: Boolean
            get() = status == Status.OPEN

        val isDirect: Boolean
            get() = status == Status.DIRECT

        val isDenied: Boolean
            get() = status == Status.DENIED

        val isExpired: Boolean
            get() = status == Status.EXPIRED

        enum class Status {
            OPEN, DIRECT, DENIED, EXPIRED
        }
    }
}

data class DebatHeader(val text: String) : ItemModel {
    override fun getType(): Int = TYPE_HEADER
}

data class DebatDetail(
    val challenger: Audience,
    val opponent: Audience?,
    val topic: String,
    val statement: String
) : Serializable