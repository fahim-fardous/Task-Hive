package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.ui.theme.hintColor
import com.example.taskhive.utils.SelectableProperties.backgroundColors
import com.example.taskhive.utils.SelectableProperties.colors
import com.example.taskhive.utils.SelectableProperties.icons
import com.example.taskhive.utils.SelectableProperties.progressIndicatorColors

@Composable
fun InProgressCard(
    projectName: String,
    taskName: String,
    progress: Float,
    projectId: Int,
    icon: ImageVector = Icons.Filled.Backpack,
    iconColor: Color = Color.Black,
    borderColor: Color = Color.Transparent,
) {
    Box(
        modifier =
            Modifier
                .width(220.dp)
                .height(120.dp)
                .background(
                    color = backgroundColors[projectId % backgroundColors.size],
                    shape = RoundedCornerShape(16.dp),
                ).padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = projectName,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    maxLines = 1,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(top = 2.dp),
                )

                Card(
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                            borderColor,
                        ),
                ) {
                    Icon(
                        icon,
                        contentDescription = "icon",
                        modifier =
                            Modifier
                                .size(30.dp)
                                .padding(4.dp),
                        tint = iconColor,
                    )
                }
            }

            Text(text = taskName, color = hintColor, maxLines = 2, overflow = TextOverflow.Ellipsis)

            LinearProgressIndicator(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .fillMaxWidth(),
                progress = { progress },
                color =
                    progressIndicatorColors[projectId % progressIndicatorColors.size],
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InProgressCardPreview() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            LazyRow {
                items(5) { id ->
                    InProgressCard(
                        projectName = "Task Hive",
                        taskName = "Design a dashboard screen with something",
                        progress = 0.5f,
                        projectId = id,
                        icon = icons[id % icons.size],
                        iconColor = colors[id],
                        borderColor = backgroundColors[id % backgroundColors.size],
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}
