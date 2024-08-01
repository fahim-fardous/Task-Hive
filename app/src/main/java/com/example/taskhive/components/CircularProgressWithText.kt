package com.example.taskhive.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressWithText(
    modifier: Modifier = Modifier,
    progress: Float = 0.85f,
    strokeWidth: Float = 4f,
    color: Color = Color.Blue,
    size:Dp = 100.dp,
    textSize:TextUnit = 18.sp,
    trackColor:Color = Color(0xFF8664FE),
    textColor:Color = Color.White,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .padding(start = 16.dp),
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
                .rotate(90f),
            color = color,
            strokeWidth = strokeWidth.dp,
            trackColor = trackColor,
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = textSize,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}
