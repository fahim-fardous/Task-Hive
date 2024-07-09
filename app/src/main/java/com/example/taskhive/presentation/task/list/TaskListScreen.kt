package com.example.taskhive.presentation.task.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.components.CalendarCard
import com.example.taskhive.components.ProgressType
import com.example.taskhive.components.Task
import com.example.taskhive.components.TopBar
import com.example.taskhive.ui.theme.TaskHiveTheme

@Composable
fun TaskListScreen() {
    TaskListScreenSkeleton()
}

@Preview(showBackground = true)
@Composable
private fun TaskListScreenSkeletonPreview() {
    TaskHiveTheme {
        TaskListScreen()
    }
}

@Composable
fun TaskListScreenSkeleton() {
    Scaffold(
        topBar =
            {
                TopBar(
                    onClick = { /*TODO*/ },
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
                item { ProgressType(onClick = { /*TODO*/ }, text = "All", isSelected = true) }
                item { ProgressType(onClick = { /*TODO*/ }, text = "To do", isSelected = false) }
                item {
                    ProgressType(
                        onClick = { /*TODO*/ },
                        text = "In Progress",
                        isSelected = false,
                    )
                }
                item {
                    ProgressType(
                        onClick = { /*TODO*/ },
                        text = "Completed",
                        isSelected = false,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(5) {
                    Task(
                        taskGroup = "Grocery shopping app design",
                        projectName = "Market Research",
                        endTime = "10:00 AM",
                        status = "Done",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
