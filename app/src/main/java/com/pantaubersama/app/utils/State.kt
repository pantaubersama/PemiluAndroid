package com.pantaubersama.app.utils

sealed class State {
    object Success: State()
    object Loading: State()
    data class Error(val message: String?): State()
}