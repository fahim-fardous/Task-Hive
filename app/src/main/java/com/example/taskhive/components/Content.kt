package com.example.taskhive.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClick: (CalendarUiModel.Date) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(data.selectedDate) {
        val index = data.visibleDates.indexOfFirst { it.date.isEqual(data.selectedDate.date) }
        if (index != -1) {
            lazyListState.animateScrollToItem(index)
        }
    }

    LazyRow(state = lazyListState) {
        items(data.visibleDates) { date ->
            ContentItem(
                date,
            ) { onDateClick(date) }
        }
    }
}
