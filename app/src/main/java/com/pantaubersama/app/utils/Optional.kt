package com.pantaubersama.app.utils

sealed class Optional<out T : Any> {
    data class Some<out T : Any>(val value: T) : Optional<T>()
    class None(val throwable: Throwable? = null) : Optional<Nothing>()
}
