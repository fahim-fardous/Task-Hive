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

@Composable
fun CalendarCard(selectedDate: (CalendarUiModel.Date) -> Unit = {}, currentDate:LocalDate) {
    val dataSource = CalendarDataSource()
    var calendarUiModel by remember {
        mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today))
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(
            data = calendarUiModel,
            onPreviousDayClick = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel =
                    dataSource.getData(
                        startDate = finalStartDate,
                        lastSelectedDate = calendarUiModel.selectedDate.date,
                    )
            },
            onNextDayClick = { endDate ->
                val finalEndDate = endDate.plusDays(1)
                calendarUiModel =
                    dataSource.getData(
                        startDate = finalEndDate,
                        lastSelectedDate = calendarUiModel.selectedDate.date,
                    )
            },
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
        })
    }
}
