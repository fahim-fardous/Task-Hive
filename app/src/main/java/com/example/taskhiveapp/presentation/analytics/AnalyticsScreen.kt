package com.example.taskhiveapp.presentation.analytics

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhiveapp.components.CommonDateRangePicker
import com.example.taskhiveapp.components.HeaderItem
import com.example.taskhiveapp.components.SelectProjectsDialog
import com.example.taskhiveapp.components.TableItem
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.presentation.task.model.ProjectUiModel
import com.example.taskhiveapp.utils.MockData
import com.example.taskhiveapp.utils.getReadableDate
import com.example.taskhiveapp.utils.getReadableTime
import com.example.taskhiveapp.utils.localDateToDate
import java.util.Date

@Composable
fun AnalyticsScreen(
    goBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel(),
) {
    // TODO Adjustable cell size
    // TODO Notification recreate issue
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getWeeklyTask()
        viewModel.getAllProjects()
    }
    val weeklyReport by viewModel.weeklyTask.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val showMessage by viewModel.showMessage.collectAsState()

    LaunchedEffect(showMessage) {
        if (showMessage != null) {
            Toast.makeText(context, showMessage, Toast.LENGTH_SHORT).show()
            viewModel.resetMessage()
        }
    }

    AnalyticsScreenSkeleton(
        context = context,
        goBack = goBack,
        tasks = weeklyReport,
        projects,
        exportData = { selectedProjects ->
            viewModel.downloadReport(context, selectedProjects)
        },
        taskByRange = { startDate, endDate ->
            viewModel.getWeeklyTask(startDate, endDate)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenSkeleton(
    context: Context = LocalContext.current,
    goBack: () -> Unit = {},
    tasks: List<Task> = emptyList(),
    projects: List<ProjectUiModel> = emptyList(),
    exportData: (List<ProjectUiModel>) -> Unit = {},
    taskByRange: (Date, Date) -> Unit = { _, _ -> },
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showSelectProjectDialog by remember { mutableStateOf(false) }
    var showDateRangePickerDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Analytics", color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = { goBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            },
            actions = {
                IconButton(onClick = { showDateRangePickerDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "date range",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                IconButton(onClick = { showDropdownMenu = true }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "show options",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false },
                    ) {
                        DropdownMenuItem(text = { Text(text = "Download report") }, onClick = {
                            showSelectProjectDialog = true
                            showDropdownMenu = false
                        })
                    }
                }
            },
        )
    }) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
            ) {
                LazyColumn {
                    item {
                        Row {
                            HeaderItem(width = 100.dp, title = "Project", isCenter = true)
                            HeaderItem(width = 300.dp, title = "Task", isCenter = true)
                            HeaderItem(width = 100.dp, title = "Tag", isCenter = true)
                            HeaderItem(width = 110.dp, title = "Start Date", isCenter = true)
                            HeaderItem(width = 100.dp, title = "Start Time", isCenter = true)
                            HeaderItem(width = 100.dp, title = "End Time", isCenter = true)
                            HeaderItem(width = 100.dp, title = "Duration", isCenter = true)
                        }
                    }
                    items(tasks) { task ->
                        TableItem(
                            projectName = task.project.name,
                            taskName = task.title,
                            tag = "Tag",
                            startDate = task.plannedStartDate.getReadableDate(),
                            startTime = task.plannedStartTime.getReadableTime(),
                            endTime = task.plannedEndTime.getReadableTime(),
                            duration = task.totalTimeSpend,
                        )
                    }
                }
            }
        }
    }

    if (showSelectProjectDialog) {
        SelectProjectsDialog(
            projects = projects,
            onDismissRequest = { showSelectProjectDialog = false },
            onConfirm = { selectedProjects ->
                println("SelectedProducts: $selectedProjects")
                exportData(selectedProjects)
                showSelectProjectDialog = false
            },
        )
    }

    if (showDateRangePickerDialog) {
        CommonDateRangePicker(
            title = "Select date range",
            onRangeSelected = { startDate, endDate ->
                taskByRange(localDateToDate(startDate), localDateToDate(endDate))
            },
            onDismiss = { showDateRangePickerDialog = false },
        )
    }
}

@Preview
@Composable
private fun AnalyticsScreenSkeletonPreview() {
    val tasks = mutableListOf<Task>()
    for (i in 1..10) {
        tasks.add(
            Task(
                id = 1,
                title = "Sample Task",
                description = "This is a sample task description",
                plannedStartTime = Date(),
                plannedEndTime = Date(),
                actualStartTime = Date(),
                actualEndTime = Date(),
                totalTimeSpend = 3600L,
                project = MockData.project,
            ),
        )
    }

    AnalyticsScreenSkeleton(
        tasks = tasks,
    )
}
