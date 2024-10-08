package com.example.taskhiveapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhiveapp.utils.SelectableProperties.backgroundColors
import com.example.taskhiveapp.utils.SelectableProperties.colors

@Composable
fun TaskGroup(
    onClick: () -> Unit,
    project: String,
    numberOfTask: Int,
    progress: Float,
    selectedIcon: Int = 0,
    selectedIconColor: Int = 0,
    selectedBorderColor: Int = 0,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    id: Int = 0,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TaskGroupIcon(
                selectedIcon,
                selectedIconColor,
                selectedBorderColor,
                size = 40.dp,
                padding = 4.dp,
            )
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
            ) {
                Text(text = project, color = MaterialTheme.colorScheme.onBackground)
                Text(
                    text = if (numberOfTask > 1) "$numberOfTask tasks" else "$numberOfTask task",
                    color = Color(0xFF6E6A7C),
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            CircularProgressWithText(
                progress = progress,
                color = colors[selectedIconColor],
                strokeWidth = 8f,
                size = 60.dp,
                textSize = 12.sp,
                trackColor = backgroundColors[selectedBorderColor],
                textColor = textColor,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskGroupPreview() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = Color.Black),
    ) {
        TaskGroup(project = "Office Project", numberOfTask = 23, progress = 0.6f, onClick = {})
    }
}
