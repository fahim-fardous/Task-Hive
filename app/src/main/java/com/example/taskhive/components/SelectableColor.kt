package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectableColor(
    isSelected: Boolean = false,
    color: Color,
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
                        color = color,
                        shape = CircleShape,
                    ).padding(12.dp)
                    .clickable {
                        onClick()
                    },
            contentAlignment = Alignment.Center,
        ) {}
    }
}

@Preview
@Composable
private fun SelectableColorPreview() {
    SelectableColor(
        isSelected = true,
        color = Color.Red,
    )
}
