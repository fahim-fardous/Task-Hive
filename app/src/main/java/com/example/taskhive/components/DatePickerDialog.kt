package com.example.taskhive.components

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDatePicker(
    title: String,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialSelectedDate =
        remember {
            val localCalender = Calendar.getInstance()
            val utcCalender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utcCalender.clear()
            utcCalender.set(
                localCalender.get(Calendar.YEAR),
                localCalender.get(Calendar.MONTH),
                localCalender.get(Calendar.DATE),
            )
            utcCalender.timeInMillis
        }

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDate,
            selectableDates =
                object : SelectableDates {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= calendar.timeInMillis

                    override fun isSelectableYear(year: Int): Boolean = year >= calendar.get(Calendar.YEAR)
                },
        )
    val datePickerConfirmButtonEnabled =
        remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(
            onClick = {
                onDismiss()
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(it)
                }
            },
            enabled = datePickerConfirmButtonEnabled.value,
        ) {
            Text(text = "OK")
        }
    }, dismissButton = {
        TextButton(onClick = { onDismiss() }) {
            Text(text = "Cancel")
        }
    }) {
        DatePicker(state = datePickerState, title = {
            Text(
                text = title,
                Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
            )
        })
    }
}
