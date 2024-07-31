package com.example.taskhive.presentation.log.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Log
import com.example.taskhive.presentation.log.list.elements.LogItem

@Composable
fun LogListScreen(
    taskId: Int,
    goToAddLog: (Int) -> Unit = {},
    goBack: () -> Unit = {},
    viewModel: LogListViewModel = viewModel(),
) {
    println("From log list task id is $taskId")
    LaunchedEffect(Unit) {
        viewModel.getLogs(taskId)
    }
    val logs by viewModel.logs.collectAsState()
    LogListScreenSkeleton(
        logs = logs,
        goBack = goBack,
    )
}

@Preview
@Composable
private fun LogListScreenSkeletonPreview() {
    LogListScreen(taskId = 1)
}

@Composable
fun LogListScreenSkeleton(
    logs: List<Log> = emptyList(),
    goBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Logs",
                onClick = { goBack() },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add log")
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
        ) {
            LazyColumn {
                items(logs) { log ->
                    LogItem(
                        isFirst = logs.indexOf(log) == 0,
                        startTime = log.startTime,
                        endTime = log.endTime,
                        duration = log.duration,
                        taskId = log.taskId,
                    )
                }
            }
        }
    }
}