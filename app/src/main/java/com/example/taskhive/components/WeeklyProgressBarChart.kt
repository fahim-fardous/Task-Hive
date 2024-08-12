package com.example.taskhive.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.domain.model.Day
import com.example.taskhive.domain.model.ProjectProgress
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.convertMillisecondsToHoursFloat

@Composable
fun WeeklyProgressBarChart(progressList: List<Day>) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var chartData: List<Pair<String, Float>> = emptyList()
    progressList.forEach { _ ->
        chartData = days.map { day ->
            val matchingProgress = progressList.find { progress ->
                day == progress.date.toString().take(3)
            }
            val hoursSpent = matchingProgress?.totalTimeSpend?.let {
                convertMillisecondsToHoursFloat(it)
            } ?: 0f
            println(hoursSpent)
            Pair(day, hoursSpent)
        }
    }
    val spacingFromLeft = 100f
    val spacingFromBottom = 60f

    val upperValue =
        remember {
            (chartData.maxOfOrNull { it.second }?.plus(40))?.let { it + (40 - it % 40) } ?: 0
        }
    val lowerValue = 0

    val density = LocalDensity.current

    val textPaint =
        remember(density) {
            android.graphics.Paint().apply {
                color = appColor.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = density.run { 12.sp.toPx() }
            }
        }

    Canvas(
        modifier =
        Modifier
            .fillMaxSize()
            .height(400.dp)
            .background(androidx.compose.ui.graphics.Color.White)
            .padding(16.dp),
    ) {
        val canvasHeight = size.height
        val canvasWidth = size.width

        val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size
        val barWidth = 80f

        chartData.forEachIndexed { i, chartPair ->
            val xPos = spacingFromLeft + i * spacePerData + (spacePerData - barWidth) / 2

            drawRoundRect(
                color = androidx.compose.ui.graphics.Color.Magenta,
                topLeft =
                    Offset(
                        xPos,
                        canvasHeight - spacingFromBottom - (chartPair.second / upperValue.toFloat()) * (canvasHeight - spacingFromBottom),
                    ),
                size =
                    Size(
                        barWidth,
                        (chartPair.second / upperValue.toFloat()) * (canvasHeight - spacingFromBottom),
                    ),
                cornerRadius = CornerRadius(10f, 10f),
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.second.toString(),
                    xPos + barWidth / 2,
                    canvasHeight - spacingFromBottom - (chartPair.second / upperValue.toFloat()) * (canvasHeight - spacingFromBottom) - 10f,
                    textPaint,
                )
            }

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.first,
                    xPos + barWidth / 2,
                    canvasHeight,
                    textPaint,
                )
            }
        }

        val stepSize = 40
        val steps = (lowerValue..upperValue as Int step stepSize).toList()

        steps.forEachIndexed { i, step ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    step.toString(),
                    50f,
                    canvasHeight - spacingFromBottom - i * (canvasHeight - spacingFromBottom) / (steps.size - 1),
                    textPaint,
                )
            }
        }

        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(spacingFromLeft, 0f),
            color = androidx.compose.ui.graphics.Color.Black,
            strokeWidth = 3f,
        )

        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(canvasWidth - 40f, canvasHeight - spacingFromBottom),
            color = androidx.compose.ui.graphics.Color.Black,
            strokeWidth = 3f,
        )
    }
}


