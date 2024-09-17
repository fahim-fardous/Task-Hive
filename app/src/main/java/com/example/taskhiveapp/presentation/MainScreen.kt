package com.example.taskhiveapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.taskhiveapp.MainNavHost
import com.example.taskhiveapp.Screen

@Composable
fun MainScreen(startDestination: String = Screen.OnBoard.route) {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        MainNavHost(
            navController = navController,
            startDestination = startDestination,
        )
    }
}
