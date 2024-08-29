package com.example.taskhive

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.taskhive.components.CalendarPreferences
import com.example.taskhive.presentation.MainScreen
import com.example.taskhive.presentation.splash.SplashViewModel
import com.example.taskhive.ui.theme.TaskHiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var calendarPreferences: CalendarPreferences
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0,
            )
        }

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        mainActivityViewModel.incompleteTask()
        calendarPreferences = CalendarPreferences(this)
        setContent {
            TaskHiveTheme {
                val startDestination by viewModel.startDestination.collectAsState()
                MainScreen(
                    startDestination = startDestination,
                )
            }
        }
    }

    override fun onStop() {
        println("Calling from onStop")
        calendarPreferences.clearSelectedDate()
        super.onStop()
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    TaskHiveTheme {
        MainScreen()
    }
}
