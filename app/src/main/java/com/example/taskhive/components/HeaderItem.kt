package com.example.taskhive.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HeaderItem(
    width: Dp = 70.dp,
    title: String = "Project",
    isCenter: Boolean = false
) {
    val height = if(isCenter) 64.dp else 80.dp
    Box(
        modifier =
        Modifier
            .width(width)
            .border(1.dp, color = MaterialTheme.colorScheme.onBackground)
            .height(height = height)
            .padding(horizontal = if (isCenter) 0.dp else 16.dp, vertical = 16.dp),
        contentAlignment = if (isCenter) Alignment.Center else Alignment.CenterStart
    ) {
        Text(text = title, maxLines = 2)
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderItemPreview() {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
        HeaderItem(width = 100.dp, title = "Project")
        HeaderItem(width = 300.dp, title = "Task")
    }
}
