package com.example.taskhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Date

@Composable
fun CalendarCard(
    selectedDate: (CalendarUiModel.Date) -> Unit = {},
    calendarPreferences: CalendarPreferences,
    onCalendarClick: () -> Unit = {},
    onRangeClick: () -> Unit,
) {
    // Getting the saved date from preferences
    val savedSelectedDate = remember { calendarPreferences.getSelectedDate() }
    val dataSource = CalendarDataSource()

    // Updating the UI state with the data
    var calendarUiModel by remember {
        mutableStateOf(dataSource.getData(lastSelectedDate = savedSelectedDate ?: dataSource.today))
    }

    // Update column layout
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(
            data = calendarUiModel,
            onCalendarClick = onCalendarClick,
            onRangeClick = onRangeClick,
        )

        // Updating the selected date and saving it in preferences
        Content(data = calendarUiModel, onDateClick = { date ->
            calendarUiModel = calendarUiModel.copy(
                selectedDate = date,
                visibleDates = calendarUiModel.visibleDates.map {
                    it.copy(isSelected = it.date.isEqual(date.date))
                }
            )
            selectedDate(date)
            calendarPreferences.saveSelectedDate(date.date)
        })
    }
}
