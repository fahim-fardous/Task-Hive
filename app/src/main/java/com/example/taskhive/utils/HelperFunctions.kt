package com.example.taskhive.utils

import java.text.SimpleDateFormat
import java.util.Date

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
}