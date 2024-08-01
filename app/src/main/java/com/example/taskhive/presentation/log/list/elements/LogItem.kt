package com.example.taskhive.presentation.log.list.elements

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.components.TaskGroupIcon
import com.example.taskhive.utils.formatLogTime
import com.example.taskhive.utils.getReadableTime
import java.util.Date

@Composable
fun LogItem(
    modifier: Modifier = Modifier,
    isFirst: Boolean,
    startTime: Date,
    endTime: Date,
    duration: Long,
    taskId: Int = 0,
) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!isFirst) {
                Box(
                    modifier =
                        modifier
                            .width(1.dp)
                            .height(50.dp)
                            .background(color = Color(0xFFC9C7C7)),
                )
            }
            TaskGroupIcon()
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            Text(
                text = formatLogTime(duration),
                color = Color.LightGray,
                fontSize = 12.sp,
                maxLines = 1,
            )
            Row {
                Text(
                    startTime.getReadableTime(),
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = " - ${endTime.getReadableTime()}",
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogItemPreview() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        LogItem(isFirst = true, startTime = Date(), endTime = Date(), duration = 10)
        LogItem(isFirst = false, startTime = Date(), endTime = Date(), duration = 12)
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LogItemPreviewDark() {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LogItem(isFirst = true, startTime = Date(), endTime = Date(), duration = 10)
        LogItem(isFirst = false, startTime = Date(), endTime = Date(), duration = 12)
    }
}
