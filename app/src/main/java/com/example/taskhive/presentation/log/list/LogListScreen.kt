package com.example.taskhive.presentation.log.list

import android.content.res.Configuration
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.TimePickerDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Log
import com.example.taskhive.presentation.log.list.elements.LogItem
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.HelperFunctions.convert24HourTo12Hour
import com.example.taskhive.utils.MockData
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime
import com.example.taskhive.utils.toDate
import java.util.Date

@Composable
fun LogListScreen(
    taskId: Int,
    goToAddLog: (Int) -> Unit = {},
    goBack: () -> Unit = {},
    viewModel: LogListViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getLogs(taskId)
    }
    val logs by viewModel.logs.collectAsState()
    LogListScreenSkeleton(
        logs = logs,
        goBack = goBack,
        saveLog = { startTime, endTime, startDate, endDate ->
            viewModel.saveLog(
                Log(
                    startTime = startTime,
                    endTime = endTime,
                    taskId = taskId,
                    duration = endTime.time - startTime.time,
                    startDate = startDate,
                    endDate = endDate,
                ),
            )
        },
    )
}

@Preview
@Composable
private fun LogListScreenSkeletonPreview() {
    LogListScreenSkeleton(
        logs =
            List(10) {
                MockData.log
            },
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LogListScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        LogListScreenSkeleton(
            logs =
                List(10) {
                    MockData.log
                },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogListScreenSkeleton(
    logs: List<Log> = emptyList(),
    goBack: () -> Unit = {},
    saveLog: (Date, Date, Date, Date) -> Unit = { _, _, _, _ -> },
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    var startTime by remember { mutableStateOf<Date?>(null) }
    var endTime by remember { mutableStateOf<Date?>(null) }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var showStartDatePickerDialog by remember { mutableStateOf(false) }
    var showEndDatePickerDialog by remember { mutableStateOf(false) }
    var showStartTimePickerDialog by remember { mutableStateOf(false) }
    var showEndTimePickerDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                title = "Logs",
                onClick = { goBack() },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add log")
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
        ) {
            LazyColumn {
                items(logs) { log ->
                    println("-------------------------")
                    println(log.endTime.getReadableTime())
                    LogItem(
                        isFirst = logs.indexOf(log) == 0,
                        startTime = log.startTime,
                        endTime = log.endTime,
                        duration = log.duration,
                        taskId = log.taskId,
                    )
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = startTime.getReadableTime(),
                        onValueChange = {},
                        placeholder = { Text(text = "Start Time") },
                        maxLines = 1,
                        readOnly = true,
                    )
                    FloatingActionButton(onClick = { showStartTimePickerDialog = true }) {
                        Icon(Icons.Filled.Alarm, contentDescription = "Add time")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = endTime.getReadableTime(),
                        onValueChange = {},
                        placeholder = { Text(text = "End Time") },
                        maxLines = 1,
                        readOnly = true,
                    )
                    FloatingActionButton(onClick = { showEndTimePickerDialog = true }) {
                        Icon(Icons.Filled.Alarm, contentDescription = "Add time")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = startDate.getReadableDate(),
                        onValueChange = {},
                        placeholder = { Text(text = "End Date") },
                        maxLines = 1,
                        readOnly = true,
                    )
                    FloatingActionButton(onClick = { showStartDatePickerDialog = true }) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Add time")
                    }
                }
                CustomButton(text = "Save Log", onClick = {
                    showBottomSheet = false
                    if (startTime != null && endTime != null && startDate != null) {
                        if (startTime?.after(endTime) == true) {
                            Toast
                                .makeText(
                                    context,
                                    "Start time cannot be after end time",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        } else {
                            saveLog(startTime!!, endTime!!, startDate!!, endDate!!)
                        }
                    }
                })
            }
        }
    }
    // -----------------------------------------------------------------------------------
    // Dialog
    // -----------------------------------------------------------------------------------

    if (showStartTimePickerDialog) {
        val calendar = Calendar.getInstance()
        val timePickerState =
            rememberTimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = false,
            )
        TimePickerDialog(
            onDismissRequest = {
                showStartTimePickerDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    startTime = convert24HourTo12Hour("$hour:$minute").toDate()!!
                    showStartTimePickerDialog = false
                }) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showStartTimePickerDialog = false
                }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (showEndTimePickerDialog) {
        val calendar = Calendar.getInstance()
        val timePickerState =
            rememberTimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = false,
            )
        TimePickerDialog(
            onDismissRequest = {
                showEndTimePickerDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    endTime = convert24HourTo12Hour("$hour:$minute").toDate()!!
                    showEndTimePickerDialog = false
                }) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEndTimePickerDialog = false
                }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (showStartDatePickerDialog) {
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

        DatePickerDialog(onDismissRequest = { showStartDatePickerDialog = false }, confirmButton = {
            TextButton(
                onClick = {
                    showStartDatePickerDialog = false

                    datePickerState.selectedDateMillis?.let {
                        startDate = Date(it)
                    }
                },
                enabled = datePickerConfirmButtonEnabled.value,
            ) {
                Text(text = "OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showStartDatePickerDialog = false }) {
                Text(text = "Cancel")
            }
        }) {
            DatePicker(state = datePickerState, title = {
                Text(
                    text = "Task Date",
                    Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                )
            })
        }
    }
}
