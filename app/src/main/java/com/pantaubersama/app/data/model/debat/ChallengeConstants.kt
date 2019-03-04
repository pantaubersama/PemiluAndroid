package com.pantaubersama.app.data.model.debat

object ChallengeConstants {
    object Progress {
        const val CHALLENGE = "challenge"
        const val COMING_SOON = "coming_soon"
        const val LIVE_NOW = "live_now"
        const val DONE = "done"
        const val WAITING_OPPONENT = "waiting_opponent"
        const val WAITING_CONFIRMATION = "waiting_confirmation"
    }

    object Status {
        const val LIVE_NOW = "LIVE NOW"
        const val COMING_SOON = "COMING SOON"
        const val DONE = "DONE"
        const val OPEN_CHALLENGE = "OPEN CHALLENGE"
        const val DIRECT_CHALLENGE = "DIRECT CHALLENGE"
        const val EXPIRED = "EXPIRED"
        const val DENIED = "DENIED"
    }

    object Condition {
        const val ONGOING = "ongoing"
        const val EXPIRED = "expired"
        const val REJECTED = "rejected"
    }

    object Role {
        const val CHALLENGER = "challenger"
        const val OPPONENT = "opponent"
        const val OPPONENT_CANDIDATE = "opponent_candidate"
    }

    object Type {
        const val OPEN_CHALLENGE = "OpenChallenge"
        const val DIRECT_CHALLENGE = "DirectChallenge"
    }
}