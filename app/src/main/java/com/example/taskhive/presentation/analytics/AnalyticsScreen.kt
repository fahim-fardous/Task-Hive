package com.example.taskhive.presentation.analytics

import android.content.Context
import android.content.Intent
import android.os.Environment
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.HeaderItem
import com.example.taskhive.components.TableItem
import com.example.taskhive.domain.model.Task
import com.example.taskhive.utils.MockData
import com.example.taskhive.utils.formatDateToDDMMYYYY
import com.example.taskhive.utils.formatTime
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Date

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    // TODO Adjustable cell size
    // TODO Notification recreate issue
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getWeeklyTask()
    }
    val weeklyReport by viewModel.weeklyTask.collectAsState()

    AnalyticsScreenSkeleton(context = context, tasks = weeklyReport)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenSkeleton(
    context: Context = LocalContext.current,
    tasks: List<Task> = emptyList(),
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Analytics") }, actions = {
            IconButton(onClick = { showDropdownMenu = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "show options")
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                ) {
                    DropdownMenuItem(text = { Text(text = "Download report") }, onClick = {
                        downloadReport(context, tasks)
                        showDropdownMenu = false
                    })
                }
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
}

private fun downloadReport(
    context: Context,
    tasks: List<Task>,
) {
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    coroutineScope.launch {
        val csvContent = generateCsvContent(tasks)
        val csvFile = saveCsvFile(context, csvContent)

        if (csvFile != null) {
            shareCsvFile(context, csvFile)
        } else {
            // Handle the error
            Toast.makeText(context, "Error saving CSV file", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun generateCsvContent(tasks: List<Task>): String {
    val csvBuilder = StringBuilder()
    csvBuilder.append("Project, Task, Tag, Start Date, Start Time, End Time, Duration\n")
    for (task in tasks) {
        csvBuilder.append(
            "${task.project.name}, ${task.title}, Tag, ${formatDateToDDMMYYYY(task.plannedStartDate!!)}, ${task.plannedStartTime.getReadableTime()}, ${task.plannedEndTime.getReadableTime()}, ${
                formatTime(
                    task.totalTimeSpend,
                )
            }\n",
        )
    }
    return csvBuilder.toString()
}

private fun saveCsvFile(
    context: Context,
    csvContent: String,
): File? {
    val fileName = "task_report.csv"
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

    try {
        val fileWriter = FileWriter(file)
        fileWriter.append(csvContent)
        fileWriter.flush()
        fileWriter.close()
        return file
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

private fun shareCsvFile(
    context: Context,
    file: File,
) {
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val shareIntent =
        Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(fileUri, "text/csv")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
    context.startActivity(Intent.createChooser(shareIntent, "Share CSV File"))
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
