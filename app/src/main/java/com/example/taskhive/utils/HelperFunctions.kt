package com.example.taskhive.utils

import android.app.Activity
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object HelperFunctions {
    fun convert24HourTo12Hour(time24: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("HH:mm")
        val outputFormat = SimpleDateFormat("hh:mm a")

        // Parse the input time string to a Date object
        val date: Date? = inputFormat.parse(time24)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun isAfter12AM(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Create a calendar instance representing 12:00 AM of the same day
        val midnightCalendar = Calendar.getInstance()
        midnightCalendar.time = date
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 0)
        midnightCalendar.set(Calendar.MINUTE, 0)
        midnightCalendar.set(Calendar.SECOND, 0)
        midnightCalendar.set(Calendar.MILLISECOND, 0)

        // Compare the given time with 12:00 AM
        return calendar.after(midnightCalendar)
    }

    fun getDateTimeFromMillis(
        millis: Long,
        pattern: String,
    ): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(Date(millis))
    }

}
