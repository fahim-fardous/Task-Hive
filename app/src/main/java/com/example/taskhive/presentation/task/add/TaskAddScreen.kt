package com.example.taskhive.presentation.task.add

import android.content.res.Configuration
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.TimePickerDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.HelperFunctions.convert24HourTo12Hour
import com.example.taskhive.utils.getReadableTime
import com.example.taskhive.utils.toDate
import java.util.Date

@Composable
fun TaskAddScreen(
    goBack: () -> Unit,
    projectId: Int,
    viewModel: TaskAddViewModel = hiltViewModel(),
) {
    val showMessage by viewModel.showMessage.collectAsState()
    LaunchedEffect(showMessage) {
        if (showMessage != null) {
            if (showMessage == "Task saved") {
                goBack()
            }
        }
    }

    TaskAddScreenSkeleton(
        goBack = goBack,
        saveTask = { title, description, startTime, endTime ->
            viewModel.saveTask(
                id = 0,
                title = title,
                description = description,
                plannedStartTime = startTime ?: Date(),
                plannedEndTime = endTime ?: Date(),
                projectId = projectId,
            )
        },
    )
}

@Preview
@Composable
private fun TaskAddScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskAddScreenSkeleton()
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        TaskAddScreenSkeleton()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddScreenSkeleton(
    goBack: () -> Unit = {},
    saveTask: (String, String, Date?, Date?) -> Unit = { _, _, _, _ -> },
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf<Date?>(null) }
    var endTime by remember { mutableStateOf<Date?>(null) }
    var showStartTimePickerDialog by remember { mutableStateOf(false) }
    var showEndTimePickerDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                onClick = { goBack() },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                title = "Add Task",
                trailingIcon = Icons.Filled.Notifications,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick =
                    {
                        saveTask(
                            title,
                            description,
                            startTime,
                            endTime,
                        )
                    },
                text = "Add Task",
                trailingIcon = null,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState()),
        ) {
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = "Task Title",
                lines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = "Description",
                lines = 5,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CommonCard(
                    modifier = Modifier.weight(1f),
                    value = startTime.getReadableTime(),
                    onValueChange = { },
                    label = "Start Time",
                    lines = 1,
                    readOnly = true,
                )
                FloatingActionButton(onClick = { showStartTimePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CommonCard(
                    modifier = Modifier.weight(1f),
                    value = endTime.getReadableTime(),
                    onValueChange = { },
                    label = "End Time",
                    lines = 1,
                    readOnly = true,
                )
                FloatingActionButton(onClick = { showEndTimePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
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
                    startTime = convert24HourTo12Hour("$hour:$minute").toDate()
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
                    endTime = convert24HourTo12Hour("$hour:$minute").toDate()
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
}
