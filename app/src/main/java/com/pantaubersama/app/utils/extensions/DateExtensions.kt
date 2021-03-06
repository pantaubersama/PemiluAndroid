package com.pantaubersama.app.utils.extensions

import android.content.Context
import java.util.Calendar

/**
 * @author edityomurti on 10/04/2019 20:32
 */

fun Context.isVotingDay(): Boolean {
    return Calendar.getInstance().timeInMillis >= getVotingDay()
}

fun Context.isDueTime(timeInMillis: Long): Boolean {
    return Calendar.getInstance().timeInMillis >= timeInMillis
}

fun Context.getVotingDay(): Long {
    return 1555434000000 // 1555434000000 == Wed Apr 17 2019 00:00:00 GMT+7
}