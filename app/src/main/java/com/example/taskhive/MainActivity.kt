package com.example.taskhive

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.taskhive.components.CalendarPreferences
import com.example.taskhive.presentation.MainScreen
import com.example.taskhive.presentation.splash.SplashViewModel
import com.example.taskhive.service.TimerService
import com.example.taskhive.ui.theme.TaskHiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var calendarPreferences: CalendarPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0,
            )
        }

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        calendarPreferences = CalendarPreferences(this)
        // TODO Question here
        println("==============")
        println(calendarPreferences.getSelectedDate())
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
