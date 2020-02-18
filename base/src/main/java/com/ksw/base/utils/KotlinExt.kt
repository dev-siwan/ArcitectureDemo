package com.ksw.base.utils

import android.content.Context
import androidx.annotation.StringRes
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

fun <T1, T2, R>guard(first: T1?, second: T2?, block: Pair<T1, T2>.() -> R): R? =
    if (first != null && second != null) Pair(first, second).block() else null

fun Calendar.format(context: Context, @StringRes resId: Int) = format(context, resId, Locale.getDefault())
fun Calendar.format(context: Context, @StringRes resId: Int, locale: Locale) = format(context.getString(resId), locale)
fun Calendar.format(pattern: String) = format(pattern, Locale.getDefault())
fun Calendar.format(pattern: String, locale: Locale) = SimpleDateFormat(pattern, locale).format(time)
fun Calendar.week() = this[DAY_OF_WEEK]

fun Calendar.getFirstMonthOfWeek(): Int {
    val cal = clone() as Calendar
    cal[DAY_OF_MONTH] = 1
    return cal.week()
}

fun now() = Calendar.getInstance()
fun nowWeek() = now().week()
fun nowFormat(pattern: String) = now().format(pattern)
fun Calendar.isToday() = Calendar.getInstance().let {
    this[DAY_OF_YEAR] == it[DAY_OF_YEAR] && this[YEAR] == it[YEAR]
}

fun List<Calendar>.containsDate(cal: Calendar): Boolean {
    forEach {
        if (it[DAY_OF_MONTH] == cal[DAY_OF_MONTH] && it[YEAR] == cal[YEAR]) {
            return true
        }
    }

    return false
}