package com.example.taskhive.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Content(data:CalendarUiModel) {
    LazyRow {
        items(data.visibleDates){date->
            ContentItem(date)
        }
    }
}
