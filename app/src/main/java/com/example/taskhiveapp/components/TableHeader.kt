package com.example.taskhiveapp.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TableHeader() {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        HeaderItem(width = 100.dp, title = "Project")
        HeaderItem(width = 300.dp, title = "Task")
        HeaderItem(width = 100.dp, title = "Tag")
        HeaderItem(width = 100.dp, title = "Start Date")
        HeaderItem(width = 100.dp, title = "Start Time")
        HeaderItem(width = 100.dp, title = "End Time")
        HeaderItem(width = 100.dp, title = "Duration")
    }
}

@Preview
@Composable
private fun TableHeaderPreview() {
    TableHeader()
}
