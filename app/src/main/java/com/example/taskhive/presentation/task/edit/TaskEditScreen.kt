package com.example.taskhive.presentation.task.edit

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.TimePickerDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.utils.HelperFunctions.convert24HourTo12Hour
import com.example.taskhive.utils.getReadableTime
import com.example.taskhive.utils.toDate
import java.util.Date

@Composable
fun TaskEditScreen(
    goBack: () -> Unit,
    taskId: Int,
    viewModel: TaskEditViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getTaskById(taskId)
    }
    val task by viewModel.task.collectAsState()
    TaskEditScreenSkeleton(
        goBack = goBack,
        title = task?.title,
        description = task?.description,
        startTime = task?.plannedStartTime,
        endTime = task?.plannedEndTime,
        saveTask = { title, description, startTime, endTime ->
        },
    )
}

@Preview
@Composable
private fun TaskAddScreenSkeletonPreview() {
    TaskEditScreenSkeleton()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreenSkeleton(
    title: String? = null,
    description: String? = null,
    startTime: Date? = null,
    endTime: Date? = null,
    goBack: () -> Unit = {},
    saveTask: (String?, String?, Date?, Date?) -> Unit = { _, _, _, _ -> },
) {
    var newTitle by remember { mutableStateOf(title) }
    var newdescription by remember { mutableStateOf(description) }
    var newStartTime by remember { mutableStateOf<Date?>(startTime) }
    var newEndTime by remember { mutableStateOf<Date?>(endTime) }
    var newShowStartTimePickerDialog by remember { mutableStateOf(false) }
    var newShowEndTimePickerDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                onClick = { goBack() },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                title = "Edit Task",
                trailingIcon = Icons.Filled.Delete,
                isBadgeVisible = false,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(
                onClick =
                    {
                        saveTask(
                            newTitle,
                            newdescription,
                            newStartTime,
                            newEndTime,
                        )
                    },
                text = "Edit Task",
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
                value = newTitle ?: "",
                onValueChange = { newTitle = it },
                label = "Task Title",
                lines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                value = newdescription ?: "",
                onValueChange = { newdescription = it },
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
                FloatingActionButton(onClick = { newShowStartTimePickerDialog = true }) {
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
                FloatingActionButton(onClick = { newShowEndTimePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Task Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProgressType(onClick = {}, text = "To-do", true)
                ProgressType(onClick = {}, text = "In Progress", false)
                ProgressType(onClick = {}, text = "Done", false)
            }
        }
    }

    // -----------------------------------------------------------------------------------
    // Dialog
    // -----------------------------------------------------------------------------------

    if (newShowStartTimePickerDialog) {
        val calendar = Calendar.getInstance()
        val timePickerState =
            rememberTimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = false,
            )
        TimePickerDialog(
            onDismissRequest = {
                newShowStartTimePickerDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    newStartTime = convert24HourTo12Hour("$hour:$minute").toDate()
                    newShowStartTimePickerDialog = false
                }) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    newShowStartTimePickerDialog = false
                }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (newShowEndTimePickerDialog) {
        val calendar = Calendar.getInstance()
        val timePickerState =
            rememberTimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = false,
            )
        TimePickerDialog(
            onDismissRequest = {
                newShowEndTimePickerDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    newEndTime = convert24HourTo12Hour("$hour:$minute").toDate()
                    newShowEndTimePickerDialog = false
                }) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    newShowEndTimePickerDialog = false
                }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
