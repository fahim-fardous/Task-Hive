package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
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
import com.example.taskhive.ui.theme.appColor

@Composable
fun ProgressCard(
    onViewTaskClick: () -> Unit,
    progress: Float,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(color = appColor, shape = RoundedCornerShape(32.dp))
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(.5f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = if (progress <= 0.5f) "Complete your today's task" else "Your today's task almost done",
                color = Color.White,
                fontSize = 18.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier =
                Modifier
                    .background(color = Color(0xFFEDE8FE), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .clickable {
                        onViewTaskClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "View Task", color = appColor, fontWeight = FontWeight.Bold)
            }
        }
        CircularProgressWithText(
            progress = progress,
            textColor = Color.White,
            color = Color(0xFFEDE8FE),
            trackColor =
            Color(
                0xFF8664FE,
            ),
            strokeWidth = 8f,
        )
        Box(modifier = Modifier
            .weight(.25f)
            .fillMaxHeight(), contentAlignment = Alignment.TopEnd) {
            Box(modifier = Modifier
                .size(36.dp)
                .background(color = Color(0xFF9E84EC), shape = RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center){
                Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "show more", tint = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun ProgressCardPreview() {
    ProgressCard(onViewTaskClick = { /*TODO*/ }, progress = 0.85f)
}
