package com.example.taskhive.components

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskhive.domain.model.ProjectProgress

@Composable
fun WeeklyProgressBarChart(
    progressList: List<ProjectProgress>,
    maxHeight: Dp = 200.dp,
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 2.dp.toPx() }

    Row(
        modifier =
            Modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .drawBehind {
                        // draw X-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth,
                        )
                        // draw Y-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth,
                        )
                    },
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        progressList.forEach { item ->
            Bar(
                value = item,
                color = Color.BLACK,
                maxHeight = maxHeight,
            )
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: ProjectProgress,
    color: Int,
    maxHeight: Dp,
) {
    val itemHeight = remember(value) { value.totalTimeSpent * maxHeight.value / 100 }

    Spacer(
        modifier =
            Modifier
                .padding(horizontal = 5.dp)
                .height(itemHeight.dp)
                .weight(1f),
    )
}
