package com.example.taskhive.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDateRangePicker(
    title: String,
    onRangeSelected: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onRangeSelected(
                        Instant
                            .ofEpochMilli(dateRangePickerState.selectedStartDateMillis!!)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate(),
                        Instant
                            .ofEpochMilli(dateRangePickerState.selectedEndDateMillis!!)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate(),
                    )
                },
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss()}) {
                Text("Cancel")
            }
        },
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range",
                )
            },
            showModeToggle = false,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp),
        )
    }
}
