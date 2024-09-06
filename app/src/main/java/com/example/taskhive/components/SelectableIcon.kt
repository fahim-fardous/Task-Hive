package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectableIcon(
    isSelected: Boolean = false,
    icon: ImageVector,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier.border(
                2.dp,
                if (isSelected) Color.Black else Color.Transparent,
                CircleShape,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = Color.LightGray,
                        shape = CircleShape,
                    ).padding(12.dp)
                    .clickable {
                        onClick()
                    },
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = "", tint = Color.Black)
        }
    }
}

@Preview
@Composable
private fun SelectableIconPreview() {
    SelectableIcon(
        isSelected = true,
        icon = Icons.Filled.Home,
    )
}
