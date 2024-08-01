package com.example.taskhive.presentation.task.edit

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.TimePickerDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.presentation.task.model.TaskUiModel
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.HelperFunctions.convert24HourTo12Hour
import com.example.taskhive.utils.MockData.task
import com.example.taskhive.utils.getReadableTime
import com.example.taskhive.utils.toDate
import java.util.Date

@Composable
fun TaskEditScreen(
    goBack: () -> Unit,
    goToLogListScreen: (Int) -> Unit = {},
    taskId: Int,
    viewModel: TaskEditViewModel = viewModel(),
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getTaskById(taskId)
    }
    val task by viewModel.task.collectAsState()
    TaskEditScreenSkeleton(
        goBack = goBack,
        task = task,
        editTask = { title, description, startTime, endTime, status ->
            task
                ?.copy(
                    title = title,
                    description = description,
                    plannedStartTime = startTime,
                    plannedEndTime = endTime,
                    taskStatus = status,
                )?.let {
                    viewModel.editTask(
                        it,
                    )
                }
        },
        onTitleChange = { newTitle ->
            viewModel.onTitleChange(newTitle)
        },
        onDescriptionChange = { newDescription ->
            viewModel.onDescriptionChange(newDescription)
        },
        onTaskStatusChange = { newStatus ->
            viewModel.onTaskStatusChange(newStatus)
        },
        goToLogListScreen = goToLogListScreen,
        taskId = taskId,
    )
}

@Preview
@Composable
private fun TaskEditScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskEditScreenSkeleton(taskId = task.id, task = task)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskEditScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        TaskEditScreenSkeleton(taskId = task.id, task = task)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreenSkeleton(
    goBack: () -> Unit = {},
    task: TaskUiModel? = null,
    editTask: (String, String, Date?, Date?, TaskStatus) -> Unit = { _, _, _, _, _ -> },
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onTaskStatusChange: (TaskStatus) -> Unit = {},
    goToLogListScreen: (Int) -> Unit = { _ -> },
    taskId: Int,
) {
    var status by remember {
        mutableStateOf(TaskStatus.TODO)
    }
    var showStartTimePickerDialog by remember { mutableStateOf(false) }
    var showEndTimePickerDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                onClick = { goBack() },
                goToLogListScreen = { goToLogListScreen(taskId) },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                title = "Edit Task",
                trailingIcon = Icons.Filled.MoreVert,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick =
                    {
                        task?.let {
                            editTask(
                                task.title,
                                task.description,
                                task.plannedStartTime,
                                task.plannedEndTime,
                                task.taskStatus,
                            )
                        }
                        goBack()
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
                    .fillMaxSize(),
        ) {
            // TODO QUESTION
            if (task != null) {
                CommonCard(
                    modifier = Modifier.fillMaxWidth(),
                    value = task.title,
                    onValueChange = { onTitleChange(it) },
                    label = "Task Title",
                    lines = 1,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (task != null) {
                CommonCard(
                    modifier = Modifier.fillMaxWidth(),
                    value = task.description,
                    onValueChange = { onDescriptionChange(it) },
                    label = "Description",
                    lines = 5,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (task != null) {
                    CommonCard(
                        modifier = Modifier.weight(1f),
                        value = task.plannedStartTime.getReadableTime(),
                        onValueChange = { },
                        label = "Start Time",
                        lines = 1,
                        readOnly = true,
                    )
                }
                FloatingActionButton(onClick = { showStartTimePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (task != null) {
                    CommonCard(
                        modifier = Modifier.weight(1f),
                        value = task.plannedEndTime.getReadableTime(),
                        onValueChange = { },
                        label = "End Time",
                        lines = 1,
                        readOnly = true,
                    )
                }
                FloatingActionButton(onClick = { showEndTimePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
            }
            LaunchedEffect(Unit) {
                if (task != null) {
                    status = task.taskStatus
                }
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (task != null) {
                    ProgressType(
                        onClick = { onTaskStatusChange(TaskStatus.TODO) },
                        text = "To-do",
                        isSelected = task.taskStatus == TaskStatus.TODO,
                    )
                    ProgressType(
                        onClick = { onTaskStatusChange(TaskStatus.IN_PROGRESS) },
                        text = "In Progress",
                        isSelected = task.taskStatus == TaskStatus.IN_PROGRESS,
                    )
                    ProgressType(
                        onClick = { onTaskStatusChange(TaskStatus.DONE) },
                        text = "Done",
                        isSelected = task.taskStatus == TaskStatus.DONE,
                    )
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
                    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = calendar.get(Calendar.MINUTE)
                    if (task != null) {
                        task.plannedStartTime = convert24HourTo12Hour("$hour:$minute").toDate()
                    }
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
                    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = calendar.get(Calendar.MINUTE)
                    if (task != null) {
                        task.plannedEndTime = convert24HourTo12Hour("$hour:$minute").toDate()
                    }
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
