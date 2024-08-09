package com.example.taskhive.components

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.service.TimerService
import com.example.taskhive.ui.theme.appColor

@Composable
fun TimerButton(
    modifier: Modifier = Modifier,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    taskId: Int,
) {
    val timerMap by TimerService.taskMap.collectAsState()
    val id  = timerMap.entries.firstOrNull{ it.value.isRunning.value }?.key
    val isRunning = timerMap[taskId]?.isRunning?.value ?: false
    Box(
        modifier =
            modifier
                .background(color = appColor, shape = CircleShape)
                .clickable {
                    if(id == null){
                        onPlayClicked()
                    }else if(id == taskId){
                        onPauseClicked()
                    }

                }.padding(8.dp),
    ) {
        if (isRunning) {
           Icon(Icons.Filled.Pause, contentDescription = "pause", tint = Color(0xFFFAFAFA))
       } else {
           Icon(Icons.Filled.PlayArrow, contentDescription = "play", tint = Color(0xFFFAFAFA))
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
