package pl.inpost.recruitmenttask.core.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun ZonedDateTime.formatZonedDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("EEE | dd.MM.yy | HH:mm", Locale.getDefault())
    return formatter.format(this)
}