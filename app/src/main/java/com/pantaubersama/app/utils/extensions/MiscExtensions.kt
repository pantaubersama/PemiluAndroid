package com.pantaubersama.app.utils.extensions

import android.content.Context
import android.util.TypedValue

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.isOdd(): Boolean {
    return this % 2 == 0
}

fun Float.toDp(context: Context): Int {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources?.displayMetrics))
}