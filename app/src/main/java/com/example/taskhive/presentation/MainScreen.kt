package com.example.taskhive.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.taskhive.MainNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        MainNavHost(navController = navController)
    }
}
