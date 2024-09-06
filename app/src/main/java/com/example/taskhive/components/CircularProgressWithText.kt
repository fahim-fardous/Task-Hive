package com.example.taskhive.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressWithText(
    modifier: Modifier = Modifier,
    progress: Float,
    strokeWidth: Float = 4f,
    color: Color = Color.Blue,
    size: Dp = 90.dp,
    textSize: TextUnit = 18.sp,
    trackColor: Color = Color(0xFF8664FE),
    textColor: Color,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    println((progress * 100).toInt())
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .size(size),
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier =
                Modifier
                    .fillMaxSize(),
            color = color,
            strokeWidth = strokeWidth.dp,
            trackColor = trackColor,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = textSize,
            color = textColor,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun CircularProgressWithTextPreview() {
    CircularProgressWithText(progress = 0f, textColor = Color.White, strokeWidth = 8f)
}
