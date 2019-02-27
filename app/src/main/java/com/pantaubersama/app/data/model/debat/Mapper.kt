package com.pantaubersama.app.data.model.debat

fun Challenge.toDebatItemChallenge(): DebatItem.Challenge {
    val debatDetail = DebatDetail(getChallenger(), getOpponent(), topicList.first(), statement)
    val opponentCandidates = getOpponentCandidates()
    return DebatItem.Challenge(
        debatDetail,
        opponentCandidates.size,
        opponentCandidates.firstOrNull()?.avatar?.thumbnailSquare?.url,
        status
    )
}