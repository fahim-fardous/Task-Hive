package com.example.taskhive.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

fun Date?.getReadableDate(): String {
    if (this == null) return ""
    val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH)
    return try {
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Date?.getReadableTime(): String {
    if (this == null) return ""
    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    return try {
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun String?.toDate(): Date? {
    if (this == null) return null
    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    return try {
        simpleDateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val remainingMinutes = (totalSeconds % 3600) / 60
    val remainingSeconds = totalSeconds % 60
    return when {
        hours > 0 ->
            String.format(
                Locale.getDefault(),
                "%dh %02dm %02ds",
                hours,
                remainingMinutes,
                remainingSeconds,
            )

        remainingMinutes > 0 ->
            String.format(
                Locale.getDefault(),
                "%dm %02ds",
                remainingMinutes,
                remainingSeconds,
            )

        else -> String.format(Locale.getDefault(), "%ds", remainingSeconds)
    }
}

fun convertMillisecondsToHoursFloat(milliseconds: Long): Float {
    val totalMinutes = milliseconds / (1000 * 60).toFloat()
    val hours = totalMinutes / 60
    return hours + 100f
}

fun formatDateToDDMMYYYY(date: Date): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return formatter.format(date)
}

fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
