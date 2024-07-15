package com.example.taskhive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskhive.presentation.MainScreen
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.ui.theme.TaskHiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskHiveTheme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskHiveTheme {
        MainScreen()
    }
}