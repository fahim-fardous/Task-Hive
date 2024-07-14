package com.example.taskhive.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskhive.utils.SelectableProperties.backgroundColors
import com.example.taskhive.utils.SelectableProperties.colors
import com.example.taskhive.utils.SelectableProperties.icons

@Composable
fun TaskGroupIcon(
    icon: Int = 0,
    iconColor: Int = 0,
    backgroundColor: Int = 0,
    size: Dp = 30.dp,
    padding: Dp = 4.dp,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor =
                    backgroundColors[backgroundColor],
            ),
    ) {
        Icon(
            icons[icon],
            contentDescription = "icon",
            modifier =
                Modifier
                    .size(size)
                    .padding(padding),
            tint = colors[iconColor],
        )
    }
}
