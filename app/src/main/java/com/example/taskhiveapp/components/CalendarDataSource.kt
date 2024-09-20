package com.example.taskhiveapp.components

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

class CalendarDataSource {
    val today: LocalDate
        get() = LocalDate.now()

    fun getData(
        startDate: LocalDate = today,
        lastSelectedDate: LocalDate,
    ): CalendarUiModel {
        val firstDayOfWeek = startDate.with(DayOfWeek.MONDAY)
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toUiModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream
            .iterate(startDate) { date ->
                date.plusDays(1)
            }.limit(numOfDays)
            .collect(Collectors.toList())
    }

    private fun toUiModel(
        dateList: List<LocalDate>,
        lastSelectedDate: LocalDate,
    ): CalendarUiModel =
        CalendarUiModel(
            selectedDate = toItemUiModel(lastSelectedDate, true),
            visibleDates =
                dateList.map {
                    toItemUiModel(it, it.isEqual(lastSelectedDate))
                },
        )

    private fun toItemUiModel(
        date: LocalDate,
        isSelected: Boolean,
    ) = CalendarUiModel.Date(date, isSelected, date.isEqual(today))
}