package com.example.taskhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.util.Date

@Composable
fun CalendarCard(
    selectedDate: (CalendarUiModel.Date) -> Unit = {},
    calendarPreferences: CalendarPreferences,
    onCalendarClick: () -> Unit = {},
    onRangeClick: () -> Unit,
) {
    val savedSelectedDate = remember { calendarPreferences.getSelectedDate() }
    val dataSource = CalendarDataSource()
    var calendarUiModel by remember {
        mutableStateOf(dataSource.getData(lastSelectedDate = savedSelectedDate ?: dataSource.today))
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(
            data = calendarUiModel,
            onCalendarClick = onCalendarClick,
            onRangeClick = onRangeClick
        )
        Content(data = calendarUiModel, onDateClick = { date ->
            calendarUiModel =
                calendarUiModel.copy(
                    selectedDate = date,
                    visibleDates =
                        calendarUiModel.visibleDates.map {
                            it.copy(isSelected = it.date.isEqual(date.date))
                        },
                )
            selectedDate(date)
            calendarPreferences.saveSelectedDate(date.date)
        })
    }
}
