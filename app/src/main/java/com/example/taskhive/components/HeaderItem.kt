package com.example.taskhive.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HeaderItem(
    width: Dp = 70.dp,
    title: String = "Project",
) {
    Box(
        modifier =
        Modifier
            .width(width)
            .border(1.dp, color = MaterialTheme.colorScheme.onBackground)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
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
