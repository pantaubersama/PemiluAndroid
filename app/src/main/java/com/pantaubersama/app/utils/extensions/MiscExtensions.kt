package com.pantaubersama.app.utils.extensions

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.isOdd(): Boolean {
    return this % 2 == 0
}