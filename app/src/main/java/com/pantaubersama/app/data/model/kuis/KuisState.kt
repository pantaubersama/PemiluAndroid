package com.pantaubersama.app.data.model.kuis

import java.lang.IllegalStateException

enum class KuisState {
    FINISHED {
        override fun toString() = "finished"
    },
    IN_PROGRESS {
        override fun toString() = "in_progress"
    },
    NOT_PARTICIPATING {
        override fun toString() = "not_participating"
    };

    companion object {
        fun fromString(status: String): KuisState {
            return when (status) {
                FINISHED.toString() -> FINISHED
                IN_PROGRESS.toString() -> IN_PROGRESS
                NOT_PARTICIPATING.toString() -> NOT_PARTICIPATING
                else -> throw IllegalStateException("unknown participation status")
            }
        }
    }
}