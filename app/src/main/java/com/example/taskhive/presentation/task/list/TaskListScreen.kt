package com.example.taskhive.presentation.task.list

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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.CalendarCard
import com.example.taskhive.components.DeleteAlertDialog
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.TaskCard
import com.example.taskhive.components.TaskStatusDialog
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.presentation.task.model.TaskUiModel
import com.example.taskhive.ui.theme.TaskHiveTheme

@Composable
fun TaskListScreen(
    goBack: () -> Unit,
    goToAddTask: (Int) -> Unit = {},
    goToEditTask: (Int) -> Unit = {},
    goToLogListScreen: (Int) -> Unit = {},
    projectId: Int? = null,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        if (projectId != null) {
            viewModel.getTasksById(projectId)
        } else {
            viewModel.getAllTasks()
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
        saveLog = { log ->
            viewModel.saveLog(log)
        },
        goToLogScreen = { taskId ->
            goToLogListScreen(taskId)
        },
        deleteTask = { taskId ->
            if (projectId != null) {
                viewModel.deleteTask(taskId, projectId)
            } else {
                viewModel.deleteTask(taskId, null)
            }
        },
        changeTaskStatus = { taskId, status ->
            viewModel.changeTaskStatus(taskId, status)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskListScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskListScreenSkeleton()
    }
}

@Composable
fun TaskListScreenSkeleton(
    goBack: () -> Unit = {},
    goToAddTask: (Int) -> Unit = {},
    goToEditTask: (Int) -> Unit = {},
    projectId: Int? = null,
    tasks: List<TaskUiModel> = emptyList(),
    project: Project? = null,
    saveLog: (Log) -> Unit = {},
    goToLogScreen: (Int) -> Unit = {},
    deleteTask: (Int) -> Unit = { _ -> },
    changeTaskStatus: (Int, TaskStatus) -> Unit = { _, _ -> },
) {
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
    var currentStatus by remember {
        mutableStateOf(TaskStatus.TODO)
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
            CalendarCard()
            Spacer(modifier = Modifier.height(16.dp)) // Use a more efficient way to filter tasks
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
            LazyColumn {
                items(
                    filteredTasks,
                ) { task ->
                    logTaskId = task.id
                    TaskCard(
                        onClick = { goToEditTask(task.id) },
                        onPauseClicked = { totalSpend, timer, startTime, endTime ->
                            totalTimeSpend = totalSpend
                            logSpendTime = timer
                            saveLog(
                                Log(
                                    taskId = task.id,
                                    startTime = startTime,
                                    endTime = endTime,
                                    duration = timer,
                                ),
                            )
                        },
                        goToLogScreen = {
                            goToLogScreen(task.id)
                        },
                        projectName = task.project.name,
                        taskName = task.title,
                        duration = task.totalTimeSpend,
                        status =
                            when (task.taskStatus) {
                                TaskStatus.TODO -> "To-do"
                                TaskStatus.IN_PROGRESS -> "In Progress"
                                TaskStatus.DONE -> "Done"
                            },
                        icon = project?.selectedIcon ?: 0,
                        iconColor = project?.selectedIconColor ?: 0,
                        backgroundColor = project?.selectedBorderColor ?: 0,
                        onTaskDelete = {
                            showDeleteDialog = true
                        },
                        onTaskChangeStatus = {
                            showTaskChangeDialog = true
                            currentStatus = task.taskStatus
                        },
                        onTaskShowLogs = {
                            goToLogScreen(task.id)
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (showTaskChangeDialog) {
            TaskStatusDialog(
                currentStatus = currentStatus,
                onDismiss = { showTaskChangeDialog = false },
                onSave = { status ->
                    changeTaskStatus(logTaskId, status)
                    showTaskChangeDialog = false
                },
            )
        }
        if (showDeleteDialog) {
            DeleteAlertDialog(
                title = "Are you sure you want to delete this task?",
                onDeleteClicked = {
                    showDeleteDialog = false
                    deleteTask(logTaskId)
                },
            )
        }
    }
}
