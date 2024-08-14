package com.example.taskhive.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TableItem(
    projectName:String = "Project",
    taskName:String = "Task",
    tag:String = "Tag",
    startDate:String = "Start Date",
    startTime:String = "Start Time",
    endTime:String = "End Time",
    duration:String = "Duration",
) {
    HeaderItem(width = 100.dp, )
}
