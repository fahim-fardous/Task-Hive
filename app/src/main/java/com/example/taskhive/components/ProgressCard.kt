package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhive.ui.theme.appColor

@Composable
fun ProgressCard(
    onClick: () -> Unit,
    progress: Float,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF5F33E0), shape = RoundedCornerShape(16.dp))
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
    ) {
        Row {
            Column {
                Text(
                    text = "Your today's task\nalmost done!",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )
                Box(
                    modifier =
                        Modifier
                            .padding(top = 24.dp)
                            .background(
                                color = Color(0xFFEDE8FE),
                                shape = RoundedCornerShape(4.dp),
                            ).padding(horizontal = 8.dp, vertical = 6.dp),
                ) {
                    Text(text = "View Task", color = appColor, fontWeight = FontWeight.Bold)
                }
            }
            CircularProgressWithText(
                progress = 0.85f,
                color = Color.White,
                strokeWidth = 8f,
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd,
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF9C82F7))) {
                    Icon(
                        Icons.Filled.MoreHoriz,
                        contentDescription = "icon",
                        modifier =
                            Modifier
                                .size(30.dp)
                                .padding(2.dp),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressCardPreview() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            ProgressCard(onClick = { /*TODO*/ }, progress = 0.85f)
        }
    }
}
