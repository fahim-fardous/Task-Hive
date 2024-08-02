package com.example.taskhive.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.taskhive.MainNavHost
import com.example.taskhive.Screen

@Composable
fun MainScreen(
    startDestination: String = Screen.OnBoard.route,
    startTimer: () -> Unit = {},
    endTimer: () -> Unit = {},
) {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        MainNavHost(
            navController = navController,
            startDestination = startDestination,
            startTimer = startTimer,
            endTimer = endTimer,
        )
    }
}
