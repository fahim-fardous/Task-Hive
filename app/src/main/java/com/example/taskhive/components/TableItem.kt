package com.example.taskhive.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskhive.utils.formatTime
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime

@Composable
fun TableItem(
    projectName:String = "Project",
    taskName:String = "Task",
    tag:String = "Tag",
    startDate:String = "Start Date",
    startTime:String = "Start Time",
    endTime:String = "End Time",
    duration:Long = 0L,
) {
    Row {
        HeaderItem(width = 100.dp, title = projectName)
        HeaderItem(width = 300.dp, title = taskName)
        HeaderItem(width = 100.dp, title = "Task")
        HeaderItem(
            width = 110.dp,
            title = startDate,
        )
        HeaderItem(
            width = 100.dp,
            title = startTime,
        )
        HeaderItem(
            width = 100.dp,
            title = endTime,
        )
        HeaderItem(width = 100.dp, title = formatTime(duration))
    }
}
