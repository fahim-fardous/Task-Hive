package com.example.taskhive.components

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.LocalDate

class CalendarPreferences(
    context: Context,
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("calendar_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val SELECTED_DATE_KEY = "selected_date"
        private const val RANGE_SELECTED_KEY = "range_selected"
    }

    fun saveSelectedDate(date: LocalDate) {
        sharedPreferences.edit().putString(SELECTED_DATE_KEY, date.toString()).apply()
    }

    fun getSelectedDate(): LocalDate? {
        Log.d("Prefs", "Before removal: " + sharedPreferences.getString(SELECTED_DATE_KEY, null))
        val dateString = sharedPreferences.getString(SELECTED_DATE_KEY, null)
        return dateString?.let { LocalDate.parse(it) }
    }

    fun clearSelectedDate() {
        Log.d("Prefs", "Before removal: " + sharedPreferences.getString(SELECTED_DATE_KEY, null))
        sharedPreferences.edit().remove(SELECTED_DATE_KEY).apply()
        Log.d("Prefs", "After removal: " + sharedPreferences.getString(SELECTED_DATE_KEY, null))
    }
}
