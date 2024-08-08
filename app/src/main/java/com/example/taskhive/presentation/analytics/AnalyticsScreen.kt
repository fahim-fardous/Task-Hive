package com.example.taskhive.presentation.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskhive.components.WeeklyProgressBarChart

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getWeeklyProgress()
    }

    val weeklyProgress by viewModel.weeklyProgress.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Weekly Progress",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        WeeklyProgressBarChart(progressList = weeklyProgress)
    }
}
