package com.example.taskhive.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.formatLogTime
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun TaskCard(
    onClick: () -> Unit = {},
    onPauseClicked: (Long, Long, Date, Date) -> Unit = { _, _, _, _ -> },
    goToLogScreen: () -> Unit = {},
    projectName: String,
    taskName: String,
    duration: Long,
    status: String,
    icon: Int = 0,
    iconColor: Int = 0,
    backgroundColor: Int = 0,
    onTaskDelete: () -> Unit = {},
    onTaskChangeStatus: () -> Unit = {},
    onTaskShowLogs: () -> Unit = {},
) {
    var isRunning by remember {
        mutableStateOf(false)
    }

    var timer by remember {
        mutableLongStateOf(0L)
    }

    var startTime by remember {
        mutableStateOf<Date>(Date())
    }
    var endTime by remember {
        mutableStateOf<Date>(Date())
    }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            timer += 1000L
        }
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
                containerColor = Color.White,
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
                    color = Color(0xFF6E6A7C),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = taskName,
                    color = Color.Black,
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
                            timer = 0
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
    TaskCard(
        projectName = "Task Management and To do app design",
        taskName = "Market Research",
        duration = 0L,
        status = "In Progress",
    )
}
