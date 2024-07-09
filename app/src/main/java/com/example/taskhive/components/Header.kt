package com.example.taskhive.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Header(data: CalendarUiModel) {
    Row {
        Text(
            text =
                if (data.selectedDate.isToday) {
                    "Today"
                } else {
                    data.selectedDate.date.format(
                        DateTimeFormatter.ofPattern("MMMM dd"),
                    )
                },
            modifier =
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.ChevronLeft, contentDescription = "Previous")
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "Next")
        }
    }
}

