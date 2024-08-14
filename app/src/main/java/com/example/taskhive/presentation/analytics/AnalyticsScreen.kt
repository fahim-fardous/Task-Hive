package com.example.taskhive.presentation.analytics

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.HeaderItem
import com.example.taskhive.domain.model.Task
import com.example.taskhive.utils.MockData
import com.example.taskhive.utils.formatTime
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime
import java.util.Date

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getWeeklyTask()
    }
    val weeklyReport by viewModel.weeklyTask.collectAsState()

    AnalyticsScreenSkeleton(tasks = weeklyReport)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenSkeleton(tasks: List<Task> = emptyList()) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Analytics") }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "show options")
            }
        })
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
                        Row {
                            HeaderItem(width = 100.dp, title = task.project.name)
                            HeaderItem(width = 300.dp, title = task.title)
                            HeaderItem(width = 100.dp, title = "Task")
                            HeaderItem(
                                width = 110.dp,
                                title = task.plannedStartDate.getReadableDate(),
                            )
                            HeaderItem(
                                width = 100.dp,
                                title = task.plannedStartTime.getReadableTime(),
                            )
                            HeaderItem(
                                width = 100.dp,
                                title = task.plannedEndTime.getReadableTime(),
                            )
                            HeaderItem(width = 100.dp, title = formatTime(task.totalTimeSpend))
                        }
                    }
                }
            }
        }
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
        tasks = tasks
    )
}
