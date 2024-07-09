package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InProgressCard(
    taskGroup: String,
    projectName: String,
    progress: Float,
    id: Int,
    iconColor: Color = Color.Black,
    borderColor: Color? = Color.Transparent,
) {
    Box(
        modifier =
            Modifier
                .width(220.dp)
                .background(
                    if (id % 2 == 0) Color(0xFFE6F2FE) else Color(0xFFFEE8E0),
                    shape = RoundedCornerShape(16.dp),
                ).padding(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = taskGroup,
                    color = Color(0xFF6E6A7C),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                )
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
            }
            Text(
                text = projectName,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                maxLines = 2,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
            )

            LinearProgressIndicator(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .fillMaxWidth(),
                progress = { progress },
                color =
                    if (id % 2 == 0) {
                        Color(
                            0xFF0086FE,
                        )
                    } else {
                        Color(0xFFFE7D53)
                    },
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
                        taskGroup = "Task Group",
                        projectName = "Grocery shopping app design",
                        progress = 0.5f,
                        id = id,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}
