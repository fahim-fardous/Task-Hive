package com.example.taskhive.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClick: (CalendarUiModel.Date) -> Unit,
) {
    LazyRow {
        items(data.visibleDates) { date ->
            ContentItem(
                date,
                onDateClick
            )
        }
    }
}
