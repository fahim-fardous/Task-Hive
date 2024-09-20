package com.example.taskhiveapp.components

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CalendarUiModel(
    val selectedDate: Date,
    val visibleDates: List<Date>,
) {
    val startDate = visibleDates.first()
    val endDate = visibleDates.last()

    data class Date(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean,
    ) {
        val day: String = date.format(DateTimeFormatter.ofPattern("E"))
    }
}
