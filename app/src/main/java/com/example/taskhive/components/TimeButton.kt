package com.example.taskhive.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.ui.theme.appColor

@Composable
fun TimerButton(
    modifier: Modifier = Modifier,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {}
) {
    var play by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .background(color = appColor, shape = CircleShape)
            .clickable {
                if (play) {
                    onPauseClicked()
                } else {
                    onPlayClicked()
                }
                play = !play
            }
            .padding(8.dp)
    ) {
        if (play) {
            Icon(Icons.Filled.Pause, contentDescription = "pause", tint = Color(0xFFFAFAFA))
        } else {
            Icon(Icons.Filled.PlayArrow, contentDescription = "play", tint = Color(0xFFFAFAFA))
        }
    }
}

@Preview
@Composable
private fun TimerButtonPreview() {
    TimerButton()
}