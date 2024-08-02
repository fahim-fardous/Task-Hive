package com.example.taskhive.components

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.service.TimerService
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.formatLogTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun TaskCard(
    onClick: () -> Unit = {},
    onPauseClicked: (Long, Long, Date, Date) -> Unit = { _, _, _, _ -> },
    projectName: String,
    taskName: String,
    duration: Long,
    onTaskDelete: () -> Unit = {},
    onTaskChangeStatus: () -> Unit = {},
    onTaskShowLogs: () -> Unit = {},
    startTimer: () -> Unit = {},
    endTimer: () -> Unit = {},
) {
    var isRunning by remember {
        mutableStateOf(false)
    }

    val timer by TimerService.timer.collectAsState()

    var startTime by remember {
        mutableStateOf<Date>(Date())
    }
    var endTime by remember {
        mutableStateOf<Date>(Date())
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier =
        Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = projectName,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = taskName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier =
                        Modifier
                            .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timelapse,
                        contentDescription = null,
                        tint = appColor,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = formatLogTime(duration), color = appColor, fontSize = 14.sp)
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                IconWithDropdownMenu(
                    onDeleteClicked = { onTaskDelete() },
                    onChangeStatusClicked = { onTaskChangeStatus() },
                    onShowLogsClicked = { onTaskShowLogs() },
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(formatLogTime(timer))
                    TimerButton(
                        onPlayClicked = {
                            isRunning = true
                            startTime = Date()
                            startTimer()
                        },
                        onPauseClicked = {
                            isRunning = false
                            endTime = Date()
                            onPauseClicked(
                                duration + timer,
                                timer,
                                startTime,
                                endTime,
                            )
                            endTimer()
                        },
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
private fun TaskPreview() {
    TaskHiveTheme {
        TaskCard(
            projectName = "Task Management and To do app design",
            taskName = "Market Research",
            duration = 0L,
        )
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskPreviewDark() {
    TaskHiveTheme {
        TaskCard(
            projectName = "Task Management and To do app design",
            taskName = "Market Research",
            duration = 0L,
        )
    }
}
