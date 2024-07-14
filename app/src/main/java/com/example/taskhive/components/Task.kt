package com.example.taskhive.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.ui.theme.appColor

@Composable
fun Task(
    projectName: String,
    taskName: String,
    endTime: String,
    status: String,
    icon: Int = 0,
    iconColor: Int = 0,
    backgroundColor: Int = 0,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(160.dp),
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
                verticalArrangement = Arrangement.SpaceBetween,
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
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Alarm,
                        contentDescription = null,
                        tint = appColor,
                    )
                    Text(text = endTime, color = appColor)
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End,
            ) {
                TaskGroupIcon(
                    icon = icon,
                    iconColor = iconColor,
                    backgroundColor = backgroundColor,
                    size = 30.dp,
                    padding = 4.dp,
                )
                Spacer(modifier = Modifier.weight(1f))
                StatusBadge(
                    status = status,
                    cornerShape = 8.dp,
                    margin = 8.dp,
                    horizontalPadding = 2.dp,
                    verticalPadding = 1.dp,
                    fontSize = 12.dp,
                )
            }
        }
    }
}

@Preview()
@Composable
private fun TaskPreview() {
    Task(
        projectName = "Task Management and To do app design",
        taskName = "Market Research",
        endTime = "10:00 AM",
        status = "In Progress",
    )
}
