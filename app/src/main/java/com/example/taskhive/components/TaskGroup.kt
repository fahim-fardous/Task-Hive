package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskGroup(
    project: String,
    numberOfTask: Int,
    progress: Float,
    id: Int = 0,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors =
        CardDefaults.elevatedCardColors(
            containerColor = Color.White,
        ),
    )  {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            if (id % 2 == 0) {
                                Color(0xFFFEE3F1)
                            } else {
                                Color(0xFFECE3FE)
                            },
                    ),
            ) {
                Icon(
                    if (id % 2 == 0) Icons.Filled.Backpack else Icons.Filled.Person,
                    contentDescription = "icon",
                    modifier =
                        Modifier
                            .size(40.dp)
                            .padding(4.dp),
                    tint = if (id % 2 == 0) Color(0xFFF378B7) else Color(0xFF9160F3),
                )
            }
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
            ) {
                Text(text = project, color = Color.Black)
                Text(
                    text = "$numberOfTask tasks",
                    color = Color(0xFF6E6A7C),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            CircularProgressWithText(
                progress = .7f,
                color = if (id % 2 == 0) Color(0xFFF378B7) else Color(0xFF9160F3),
                strokeWidth = 8f,
                size = 70.dp,
                textSize = 16.sp,
                tracColor = Color.LightGray,
                textColor = Color.Black,
                fontWeight = FontWeight.Normal
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
        TaskGroup(project = "Office Project", numberOfTask = 23, progress = 0.6f)
    }
}
