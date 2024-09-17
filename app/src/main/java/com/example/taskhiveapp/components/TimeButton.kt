package com.example.taskhiveapp.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhiveapp.service.TimerService
import com.example.taskhiveapp.ui.theme.appColor

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TimerButton(
    modifier: Modifier = Modifier,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    taskId: Int,
) {
    val timerItem by TimerService.timerItem.collectAsState()
    val isRunning = timerItem?.isRunning == true && timerItem?.taskId == taskId

    Box(
        modifier =
            modifier
                .background(color = appColor, shape = CircleShape)
                .clickable {
                    if (!isRunning) {
                        onPlayClicked()
                    } else {
                        onPauseClicked()
                    }
                }.padding(8.dp),
    ) {
        if (isRunning) {
            Icon(Icons.Filled.Pause, contentDescription = "Pause", tint = Color(0xFFFAFAFA))
        } else {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color(0xFFFAFAFA))
        }
    }
}

@Preview
@Composable
private fun TimerButtonPreview() {
    TimerButton(
        taskId = 0,
    )
}
