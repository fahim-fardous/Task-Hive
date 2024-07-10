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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.components.CalendarCard
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.Task
import com.example.taskhive.components.TopBar
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.MockData

@Composable
fun TaskListScreen(
    goBack:()->Unit = {}
) {
    TaskListScreenSkeleton(goBack = goBack)
}

@Preview(showBackground = true)
@Composable
private fun TaskListScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskListScreen()
    }
}

@Composable
fun TaskListScreenSkeleton(
    goBack:()->Unit = {}
) {
    var selectedIndex by remember { mutableStateOf(0) }
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
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalendarCard()
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow {
                item {
                    ProgressType(
                        onClick = { selectedIndex = 0 },
                        text = "All",
                        isSelected = selectedIndex == 0,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedIndex = 1 },
                        text = "To do",
                        isSelected = selectedIndex == 1,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedIndex = 2 },
                        text = "In Progress",
                        isSelected = selectedIndex == 2,
                    )
                }
                item {
                    ProgressType(
                        onClick = { selectedIndex = 3 },
                        text = "Completed",
                        isSelected = selectedIndex == 3,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(
                    items =
                        when (selectedIndex) {
                            0 -> MockData.tasks
                            1 -> MockData.tasks.filter { it.status == "To-do" }
                            2 -> MockData.tasks.filter { it.status == "In Progress" }
                            3 -> MockData.tasks.filter { it.status == "Done" }
                            else -> MockData.tasks
                        },
                ) {
                    Task(
                        taskGroup = it.taskGroup,
                        projectName = it.projectName,
                        taskName = it.taskName,
                        endTime = it.endTime,
                        status = it.status,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
