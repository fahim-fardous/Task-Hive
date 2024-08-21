package com.example.taskhive.presentation.task.list

import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.Screen
import com.example.taskhive.components.CalendarCard
import com.example.taskhive.components.CalendarPreferences
import com.example.taskhive.components.DeleteAlertDialog
import com.example.taskhive.components.NoTaskCard
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.TaskCard
import com.example.taskhive.components.TaskStatusDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.presentation.task.model.TaskUiModel
import com.example.taskhive.service.TimerService
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.MockData
import com.example.taskhive.utils.localDateToDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Composable
fun TaskListScreen(
    goBack: () -> Unit,
    goToAddTask: (Int) -> Unit,
    goToEditTask: (Int) -> Unit,
    goToLogListScreen: (Int) -> Unit,
    projectId: Int? = null,
    viewModel: TaskListViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (projectId != null) {
            if (CalendarPreferences(context).getSelectedDate() == null) {
                viewModel.getTasks(LocalDate.now(ZoneOffset.UTC), projectId)
            } else {
                viewModel.getTasks(CalendarPreferences(context).getSelectedDate()!!, projectId)
            }
        } else {
            if (CalendarPreferences(context).getSelectedDate() == null) {
                viewModel.getTasks(LocalDate.now(ZoneOffset.UTC))
            } else {
                viewModel.getTasks(CalendarPreferences(context).getSelectedDate()!!)
            }
        }
    }
    LaunchedEffect(projectId) {
        if (projectId != null) {
            viewModel.getProjectById(projectId)
        }
    }

    val tasks by viewModel.tasks.collectAsState()
    val project by viewModel.project.collectAsState()
    TaskListScreenSkeleton(
        goBack = goBack,
        goToAddTask = goToAddTask,
        goToEditTask = goToEditTask,
        projectId = projectId,
        tasks = tasks,
        project = project,
        saveLog = { log, date ->
            if (projectId != null) {
                viewModel.saveLog(log, projectId, date)
            } else {
                viewModel.saveLog(log, null, date)
            }
        },
        goToLogScreen = { taskId ->
            goToLogListScreen(taskId)
        },
        deleteTask = { taskId, date ->
            if (projectId != null) {
                viewModel.deleteTask(taskId, projectId, date)
            } else {
                viewModel.deleteTask(taskId, null, date)
            }
        },
        changeTaskStatus = { taskId, status, date ->
            if (projectId != null) {
                viewModel.changeTaskStatus(
                    taskId = taskId,
                    projectId = projectId,
                    status = status,
                    date = date,
                )
            } else {
                viewModel.changeTaskStatus(
                    taskId = taskId,
                    projectId = null,
                    status = status,
                    date = date,
                )
            }
        },
        onDateChange = { date ->
            if (projectId != null) {
                viewModel.getTasks(date, projectId)
            } else {
                viewModel.getTasks(date)
            }
        },
        addTime = { timer, date ->
            viewModel.addTime(timer, date)
        },
        getTasks = { date ->
            if (projectId != null) {
                viewModel.getTasks(date, projectId)
            } else {
                viewModel.getTasks(date)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreenSkeleton(
    goBack: () -> Unit = {},
    goToAddTask: (Int) -> Unit = {},
    goToEditTask: (Int) -> Unit = {},
    projectId: Int? = null,
    tasks: List<TaskUiModel> = emptyList(),
    project: Project? = null,
    saveLog: (Log, LocalDate) -> Unit = { _, _ -> },
    goToLogScreen: (Int) -> Unit = {},
    deleteTask: (Int, LocalDate) -> Unit = { _, _ -> },
    changeTaskStatus: (Int, TaskStatus, LocalDate) -> Unit = { _, _, _ -> },
    onDateChange: (date: LocalDate) -> Unit = {},
    addTime: (Long, LocalDate) -> Unit = { _, _ -> },
    getTasks: (LocalDate) -> Unit = { _ -> },
) {
    val context = LocalContext.current
    var logTaskId by remember {
        mutableIntStateOf(-1)
    }
    var selectedTaskStatus by remember { mutableIntStateOf(0) }
    var totalTimeSpend by remember {
        mutableLongStateOf(0L)
    }
    var logSpendTime by remember {
        mutableLongStateOf(0L)
    }
    var showTaskChangeDialog by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var showCalendarDialog by remember {
        mutableStateOf(false)
    }
    var currentStatus by remember {
        mutableStateOf(TaskStatus.TODO)
    }
    var currentDate by remember {
        mutableStateOf(LocalDate.now(ZoneOffset.UTC))
    }
    val calendarPreferences = remember { CalendarPreferences(context) }
    var selectedDate by remember {
        mutableStateOf(calendarPreferences.getSelectedDate() ?: LocalDate.now(ZoneOffset.UTC))
    }
    var calendarSelectedDate by remember {
        mutableStateOf<Date?>(null)
    }
    var logStartDate by remember { mutableStateOf(localDateToDate(LocalDate.now())) }
    var logEndDate by remember { mutableStateOf(localDateToDate(LocalDate.now())) }

    val timerState by TimerService.timerItem.collectAsState()
    LaunchedEffect(key1 = timerState) {
        snapshotFlow { timerState?.isRunning }
            .collect { isRunning ->
                if (isRunning == null) {
                    getTasks(selectedDate)
                }
            }
    }
    Scaffold(
        topBar =
            {
                TopBar(
                    onClick = { goBack() },
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    title = "Today's Task",
                    trailingIcon = Icons.Filled.Notifications,
                )
            },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (projectId != null) {
                FloatingActionButton(
                    onClick = { goToAddTask(projectId) },
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalendarCard(
                selectedDate = { date ->
                    selectedTaskStatus = 0
                    currentDate = date.date
                    selectedDate = date.date
                    onDateChange(date.date)
                },
                onCalendarClick = {
                    showCalendarDialog = true
                },
                calendarPreferences = calendarPreferences,
            )
            Spacer(modifier = Modifier.height(16.dp))
            val filteredTasks =
                when (selectedTaskStatus) {
                    0 -> tasks
                    1 -> tasks.filter { it.taskStatus == TaskStatus.TODO }
                    2 -> tasks.filter { it.taskStatus == TaskStatus.IN_PROGRESS }
                    3 -> tasks.filter { it.taskStatus == TaskStatus.DONE }
                    else -> tasks
                }

            LazyRow {
                item {
                    ProgressType(
                        onClick = { selectedTaskStatus = 0 },
                        text = "All",
                        isSelected = selectedTaskStatus == 0,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedTaskStatus = 1 },
                        text = "To do",
                        isSelected = selectedTaskStatus == 1,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedTaskStatus = 2 },
                        text = "In Progress",
                        isSelected = selectedTaskStatus == 2,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedTaskStatus = 3 },
                        text = "Completed",
                        isSelected = selectedTaskStatus == 3,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (filteredTasks.isEmpty()) {
                NoTaskCard(selectedStatus = selectedTaskStatus)
            } else {
                LazyColumn {
                    if (filteredTasks.isEmpty()) {
                        item {
                            NoTaskCard(selectedStatus = selectedTaskStatus)
                        }
                    }
                    items(
                        items = filteredTasks,
                    ) { task ->
                        TaskCard(
                            onClick = { goToEditTask(task.id) },
                            onPauseClicked = { totalSpend, timer, startTime, endTime ->
                                if (task.id == timerState?.taskId) {
                                    context.stopService(
                                        Intent(context, TimerService::class.java).apply {
                                            putExtra("taskId", task.id)
                                        },
                                    )
                                    logTaskId = task.id
                                    totalTimeSpend = totalSpend
                                    logSpendTime = timer
                                    saveLog(
                                        Log(
                                            taskId = task.id,
                                            startTime = startTime,
                                            endTime = endTime,
                                            duration = timer,
                                            startDate = logStartDate,
                                            endDate = logEndDate,
                                        ),
                                        currentDate,
                                    )
                                    addTime(timer, selectedDate)
                                    getTasks(selectedDate)
                                    logEndDate = localDateToDate(LocalDate.now())
                                }
                            },
                            projectName = task.project.name,
                            taskId = task.id,
                            taskName = task.title,
                            duration = task.totalTimeSpend,
                            time = if (timerState?.taskId == task.id) timerState?.time else 0L,
                            onTaskDelete = {
                                showDeleteDialog = true
                                logTaskId = task.id
                            },
                            onTaskChangeStatus = {
                                showTaskChangeDialog = true
                                currentStatus = task.taskStatus
                                logTaskId = task.id
                            },
                            onTaskShowLogs = {
                                goToLogScreen(task.id)
                            },
                            onPlayClicked = {
                                if (timerState?.taskId == null &&
                                    (
                                        (
                                            task.plannedStartDate?.equals(
                                                localDateToDate(LocalDate.now()),
                                            ) == true
                                        ) ||
                                            (
                                                task.plannedStartDate?.before(
                                                    localDateToDate(
                                                        LocalDate.now(),
                                                    ),
                                                ) == true
                                            )
                                    )
                                ) {
                                    logStartDate = localDateToDate(LocalDate.now())
                                    context.startService(
                                        Intent(context, TimerService::class.java).apply {
                                            putExtra("taskId", task.id)
                                            putExtra("taskName", task.title)
                                        },
                                    )
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "This task is for future",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        if (showTaskChangeDialog) {
            TaskStatusDialog(
                currentStatus = currentStatus,
                onDismiss = { showTaskChangeDialog = false },
                onSave = { status ->
                    if (timerState?.taskId != logTaskId) {
                        showTaskChangeDialog = false
                        changeTaskStatus(logTaskId, status, currentDate)
                    } else {
                        Toast.makeText(context, "Task is running", Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }
        if (showDeleteDialog) {
            DeleteAlertDialog(
                showDeleteDialog = {
                    showDeleteDialog = it
                },
                title = "Are you sure you want to delete this task?",
                onDeleteClicked = {
                    if (timerState?.taskId != logTaskId) {
                        showDeleteDialog = false
                        deleteTask(logTaskId, currentDate)
                    } else {
                        Toast.makeText(context, "Task is running", Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }
        if (showCalendarDialog) {
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
                )
            val datePickerConfirmButtonEnabled =
                remember {
                    derivedStateOf { datePickerState.selectedDateMillis != null }
                }

            DatePickerDialog(onDismissRequest = { showCalendarDialog = false }, confirmButton = {
                TextButton(
                    onClick = {
                        showCalendarDialog = false

                        datePickerState.selectedDateMillis?.let {
                            calendarSelectedDate = Date(it)
                            onDateChange(
                                LocalDateTime
                                    .ofInstant(
                                        Date(it).toInstant(),
                                        ZoneOffset.UTC,
                                    ).toLocalDate(),
                            )
                        }
                    },
                    enabled = datePickerConfirmButtonEnabled.value,
                ) {
                    Text(text = "OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showCalendarDialog = false }) {
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
}

@Preview(showBackground = true)
@Composable
private fun TaskListScreenSkeletonPreview() {
    TaskHiveTheme {
        val tasks = mutableListOf<TaskUiModel>()
        repeat(5) {
            tasks.add(MockData.task)
        }
        TaskListScreenSkeleton(
            tasks = tasks,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskListScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        val tasks = mutableListOf<TaskUiModel>()
        repeat(5) {
            tasks.add(MockData.task)
        }
        TaskListScreenSkeleton(
            tasks = tasks,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListScreenSkeletonWithoutTaskPreview() {
    TaskHiveTheme {
        val tasks = mutableListOf<TaskUiModel>()

        TaskListScreenSkeleton(
            tasks = tasks,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskListScreenSkeletonWithoutTaskPreviewDark() {
    TaskHiveTheme {
        val tasks = mutableListOf<TaskUiModel>()
        TaskListScreenSkeleton(
            tasks = tasks,
        )
    }
}
