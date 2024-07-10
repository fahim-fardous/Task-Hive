package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.utils.MockData

@Composable
fun StatusBadge(
    status: String,
    cornerShape:Dp = 8.dp,
    margin:Dp = 8.dp,
    horizontalPadding:Dp = 2.dp,
    verticalPadding:Dp = 1.dp,
    fontSize:Dp = 12.dp,
    ) {
    Box(
        modifier =
            Modifier
                .background(
                    color =
                        MockData.statusBackgroundColor[status] ?: Color.White,
                    shape = RoundedCornerShape(8.dp),
                ).padding(horizontal = margin)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
    ) {
        Text(
            text = status,
            color = MockData.statusTextColor[status] ?: Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
