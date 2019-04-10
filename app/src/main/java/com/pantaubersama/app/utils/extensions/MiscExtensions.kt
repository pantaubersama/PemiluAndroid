package com.pantaubersama.app.utils.extensions

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Locale
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun <T> unSyncLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.isOdd(): Boolean {
    return this % 2 != 0
}

fun Float.toDp(context: Context): Int {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources?.displayMetrics))
}

fun String.parseDate(
    toFormat: String = "dd MMMM yyyy  '•'  HH:mm",
    fromFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    toTimeZone: TimeZone = TimeZone.getDefault(),
    fromTimeZone: TimeZone = TimeZone.getTimeZone("GMT")
): String {
    val dateFormatFrom = SimpleDateFormat(fromFormat, Locale("in", "ID"))
    dateFormatFrom.timeZone = fromTimeZone
    val dateFormatTo = SimpleDateFormat(toFormat, Locale("in", "ID"))
    dateFormatTo.timeZone = toTimeZone
    return dateFormatTo.format(dateFormatFrom.parse(this))
}

fun String.parseDateRemaining(fromFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", fromTimeZone: TimeZone = TimeZone.getTimeZone("GMT")): String {
    val dateFormatFrom = SimpleDateFormat(fromFormat, Locale("in", "ID"))
    dateFormatFrom.timeZone = fromTimeZone
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
