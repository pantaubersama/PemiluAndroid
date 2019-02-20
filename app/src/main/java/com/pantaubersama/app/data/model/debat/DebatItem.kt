package com.pantaubersama.app.data.model.debat

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_DEBAT_ITEM
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_HEADER

sealed class DebatItem : ItemModel {
    abstract val debatDetail: DebatDetail
    abstract val type: String

    override fun getType(): Int = TYPE_DEBAT_ITEM

    data class LiveNow(override val debatDetail: DebatDetail) : DebatItem() {
        override val type: String = "LIVE NOW"
    }

    data class ComingSoon(
        override val debatDetail: DebatDetail,
        val date: String,
        val startEndTime: String
    ) : DebatItem() {
        override val type: String = "COMING SOON"
    }

    data class Done(
        override val debatDetail: DebatDetail,
        val clap1: Int,
        val clap2: Int,
        val favoriteCount: Int
    ) : DebatItem() {
        override val type: String = "DONE"
    }

    data class Challenge(
        override val debatDetail: DebatDetail,
        val pendingOpponent: Int,
        val status: Status
    ) : DebatItem() {
        override val type: String = when (status) {
            Status.OPEN -> "OPEN CHALLENGE"
            Status.DIRECT -> "DIRECT CHALLENGE"
            Status.DENIED -> "DENIED"
            Status.EXPIRED -> "EXPIRED"
        }

        val isDirect: Boolean
            get() = status == Status.DIRECT

        enum class Status {
            OPEN, DIRECT, DENIED, EXPIRED
        }
    }
}

data class DebatHeader(val text: String) : ItemModel {
    override fun getType(): Int = TYPE_HEADER
}

data class DebatDetail(
    val name1: String,
    val name2: String,
    val tag: String
)