package com.example.taskhiveapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Header(
    data: CalendarUiModel,
    onCalendarClick: () -> Unit,
    onRangeClick: () -> Unit,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    isRangeSelected: Boolean,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text =
                if (isRangeSelected) {
                    "${
                        startDate!!.format(
                            DateTimeFormatter.ofPattern("MMM dd"),
                        )
                    }-${
                        endDate!!.format(
                            DateTimeFormatter.ofPattern("MMM dd"),
                        )
                    }"
                } else {
                    if (data.selectedDate.isToday) {
                        "Today"
                    } else {
                        data.selectedDate.date.format(
                            DateTimeFormatter.ofPattern("MMMM dd"),
                        )
                    }
                },
            modifier =
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
            fontWeight = FontWeight.Bold,
        )
        IconButton(onClick = { onCalendarClick() }) {
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "select date")
        }
        IconButton(onClick = { onRangeClick() }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "select date")
        }
    }
}
