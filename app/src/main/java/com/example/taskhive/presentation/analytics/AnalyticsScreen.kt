package com.example.taskhive.presentation.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhive.components.WeeklyProgressBarChart
import com.example.taskhive.domain.model.Day

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getWeeklyProgress()
    }
    val weeklyReport by viewModel.weeklyReport.collectAsState()

    AnalyticsScreenSkeleton(weeklyReport = weeklyReport)
}

@Composable
fun AnalyticsScreenSkeleton(weeklyReport:List<Day> = emptyList()) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = "Weekly Progress",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            WeeklyProgressBarChart(progressList = weeklyReport)
        }
    }
}

@Preview
@Composable
private fun AnalyticsScreenSkeletonPreview() {
    AnalyticsScreenSkeleton()
}
