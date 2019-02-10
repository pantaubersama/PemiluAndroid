package com.pantaubersama.app.data.model.debat

sealed class DebatItem {
    abstract val debatDetail: DebatDetail
    abstract val type: String

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

    data class OpenChallenge(override val debatDetail: DebatDetail) : DebatItem() {
        override val type: String = "OPEN CHALLENGE"
    }
}

data class DebatDetail(
    val name1: String,
    val name2: String,
    val tag: String
)