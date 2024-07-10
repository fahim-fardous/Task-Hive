package com.example.taskhive.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskhive.utils.MockData

@Composable
fun TaskGroupIcon(
    taskGroup:String,
    size: Dp = 30.dp,
    padding: Dp = 4.dp,
) {
    Card(
        colors =
        CardDefaults.cardColors(
            containerColor =
            MockData.taskGroupBackgroundColor[taskGroup] ?: Color.White,
        ),
    ) {
        Icon(
            MockData.taskIcon[taskGroup] ?: Icons.Filled.Alarm,
            contentDescription = "icon",
            modifier =
            Modifier
                .size(size)
                .padding(padding),
            tint = MockData.taskGroupIconColor[taskGroup] ?: Color.White,
        )
    }

}
