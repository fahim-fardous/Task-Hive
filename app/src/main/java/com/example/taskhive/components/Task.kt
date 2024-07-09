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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
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
import com.example.taskhive.ui.theme.appColor

@Composable
fun Task(
    taskGroup: String,
    projectName: String,
    endTime: String,
    status: String,
    id: Int = 0,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth().height(120.dp),
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = taskGroup, color = Color(0xFF6E6A7C))
                Text(
                    text = projectName,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold,
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
            Column(horizontalAlignment = Alignment.CenterHorizontally,
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
                            .size(30.dp)
                            .padding(4.dp),
                        tint = if (id % 2 == 0) Color(0xFFF378B7) else Color(0xFF9160F3),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier
                    .background(color = Color(0xFFECE7FE), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp)
                    .padding(horizontal = 2.dp, vertical = 1.dp)){
                    Text(text = "Done", color = appColor, fontSize = 12.sp)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Task(
            taskGroup = "Grocery shopping app design",
            projectName = "Market Research",
            endTime = "10:00 AM",
            status = "Done",
        )
        Task(
            taskGroup = "Grocery shopping app design",
            projectName = "Market Research",
            endTime = "10:00 AM",
            status = "In Progress",
        )
        Task(
            taskGroup = "Grocery shopping app design",
            projectName = "Market Research",
            endTime = "10:00 AM",
            status = "To-do",
        )
        Task(
            taskGroup = "Grocery shopping app design",
            projectName = "Market Research",
            endTime = "10:00 AM",
            status = "To-do",
        )
    }
}
