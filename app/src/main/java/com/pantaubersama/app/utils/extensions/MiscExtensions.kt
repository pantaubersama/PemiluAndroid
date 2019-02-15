package com.pantaubersama.app.utils.extensions

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)