package com.example.taskhive.presentation.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.domain.model.Task
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.lazyTablePinConfiguration

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
            LazyTable(
                pinConfiguration =
                    lazyTablePinConfiguration(
                        columns = 7, // Number of columns in the table
                        rows = tasks.size,
                        footer = true,
                    ),
                dimensions = lazyTableDimensions(148.dp, 32.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                // Items for each task
                items(
                    count = tasks.size * 7, // Adjusted for 7 columns
                    layoutInfo = {
                        LazyTableItem(
                            column = it % 7,
                            row = it / 7 + 1,
                        )
                    },
                ) {
                    Cell(task = tasks[it / 7], column = it % 7)
                }

                // Header items
                items(
                    count = 7, // Adjusted for 7 columns
                    layoutInfo = {
                        LazyTableItem(
                            column = it % 7,
                            row = 0,
                        )
                    },
                ) {
                    HeaderCell(column = it)
                }
            }
        }
    }
}

@Suppress("ComplexMethod")
@Composable
private fun Cell(
    task: Task,
    column: Int,
) {
    val content =
        when (column) {
            0 -> task.project.name
            1 -> task.title // Assuming task has a 'tag' property with a 'name'
            2 -> "Tag"
            3 -> task.plannedStartDate.getReadableDate()
            4 -> task.actualStartTime.getReadableTime()
            5 -> task.actualEndTime.getReadableTime()
            6 -> "1H 2M" // Assuming 'duration' is a property of task
            else -> error("Unknown column index: $column")
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface),
    ) {
        if (content.isNotEmpty()) {
            Text(text = content)
        }
    }
}

@Composable
private fun HeaderCell(column: Int) {
    val content =
        when (column) {
            0 -> "Project"
            1 -> "Tag"
            2 -> "Start Date"
            3 -> "Start Time"
            4 -> "End Time"
            5 -> "Duration"
            else -> error("Unknown column index: $column")
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .border(Dp.Hairline, MaterialTheme.colorScheme.onPrimary),
    ) {
        Text(text = content)
    }
}

@Preview
@Composable
private fun AnalyticsScreenSkeletonPreview() {
    AnalyticsScreenSkeleton()
}
