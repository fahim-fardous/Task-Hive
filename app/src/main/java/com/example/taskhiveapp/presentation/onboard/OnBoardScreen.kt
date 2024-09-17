package com.example.taskhiveapp.presentation.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhiveapp.R
import com.example.taskhiveapp.components.CustomButton
import com.example.taskhiveapp.ui.theme.TaskHiveTheme

@Composable
fun OnBoardScreen(
    goToHome: () -> Unit = {},
    viewModel: OnBoardViewModel,
) {
    OnBoardScreenSkeleton(
        goToHome = goToHome,
        onBoarded = {
            viewModel.setOnboardingCompleted()
        },
    )
}

@Composable
fun OnBoardScreenSkeleton(
    goToHome: () -> Unit = {},
    onBoarded: () -> Unit = {},
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {
                    goToHome()
                    onBoarded()
                },
                text = "Let's Start",
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                marginStart = 16.dp,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Image(
                painter = painterResource(id = R.drawable.onboard),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Task Management & \n To-Do List",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "This productive tool is designed to help \n you better manage your task \n project-wise conveniently",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF6E6A7C),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardScreenPreview() {
    TaskHiveTheme {
        OnBoardScreenSkeleton()
    }
}
