package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.ui.theme.appColor

@Composable
fun ProgressType(
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean,
) {
    Box(
        modifier =
            Modifier
                .padding(end = 16.dp)
                .background(
                    color = if (isSelected) appColor else Color(0xFFECE7FE),
                    shape = RoundedCornerShape(8.dp),
                ).padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else appColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressTypePreview() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProgressType(onClick = {}, text = "All", true)
        ProgressType(onClick = {}, text = "To do", false)
        ProgressType(onClick = {}, text = "In Progress", false)
    }
}
