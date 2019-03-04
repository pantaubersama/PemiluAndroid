package com.pantaubersama.app.utils.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.isOdd(): Boolean {
    return this % 2 != 0
}

fun String.parseDate(fromFormat: String = "yyyy-MM-dd'T'hh:mm:ss", toFormat: String = "dd MMMM yyyy  '•'  hh:mm"): String {
    val dateFormatFrom = SimpleDateFormat(fromFormat, Locale("in", "ID"))
    val dateFormatTo = SimpleDateFormat(toFormat, Locale("in", "ID"))
    return dateFormatTo.format(dateFormatFrom.parse(this))
}