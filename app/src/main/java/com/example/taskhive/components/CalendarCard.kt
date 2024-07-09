package com.example.taskhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalendarCard(modifier: Modifier = Modifier) {
    val dataSource = CalendarDataSource()
    val calendarUiModel = dataSource.getData(lastSelectedDate = dataSource.today)
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(data = calendarUiModel)
        Content(data = calendarUiModel)
    }
}
