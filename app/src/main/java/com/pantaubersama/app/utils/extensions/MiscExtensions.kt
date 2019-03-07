package com.pantaubersama.app.utils.extensions

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.isOdd(): Boolean {
    return this % 2 != 0
}

fun String.parseDate(toFormat: String = "dd MMMM yyyy  '•'  hh:mm", fromFormat: String = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"): String {
    val dateFormatFrom = SimpleDateFormat(fromFormat, Locale("in", "ID"))
    dateFormatFrom.timeZone = TimeZone.getTimeZone("GMT")
    val dateFormatTo = SimpleDateFormat(toFormat, Locale("in", "ID"))
    dateFormatTo.timeZone = TimeZone.getDefault()
    return dateFormatTo.format(dateFormatFrom.parse(this))
}

fun String.parseDateRemaining(fromFormat: String = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"): String {
    val dateFormatFrom = SimpleDateFormat(fromFormat, Locale("in", "ID"))
    dateFormatFrom.timeZone = TimeZone.getTimeZone("GMT")
    val msDiff = dateFormatFrom.parse(this).time - Calendar.getInstance().timeInMillis
    val daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
    val hourDiff = TimeUnit.MILLISECONDS.toHours(msDiff)
    val minuteDiff = TimeUnit.MILLISECONDS.toMinutes(msDiff)
    return when {
        daysDiff > 0.toLong() -> "$daysDiff hari"
        hourDiff > 0.toLong() -> "$hourDiff jam"
        minuteDiff > 0.toLong() -> "$minuteDiff menit"
        else -> "sesaat"
    }
}