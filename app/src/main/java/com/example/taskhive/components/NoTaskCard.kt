package com.example.taskhive.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.R

@Composable
fun NoTaskCard(selectedStatus: Int = 0) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "no task",
            modifier = Modifier.size(150.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text =
                when (selectedStatus) {
                    0 -> "No Task Found"
                    1 -> "No To Do Task Found"
                    2 -> "No In Progress Task Found"
                    3 -> "No Completed Task Found"
                    else -> "No Task Found"
                },
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun NoTaskCardPreview() {
    NoTaskCard()
}
