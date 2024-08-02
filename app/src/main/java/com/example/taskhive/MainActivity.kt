package com.example.taskhive

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.taskhive.presentation.MainScreen
import com.example.taskhive.presentation.splash.SplashViewModel
import com.example.taskhive.service.TimerService
import com.example.taskhive.ui.theme.TaskHiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        setContent {
            TaskHiveTheme {
                val startDestination by viewModel.startDestination.collectAsState()
                    MainScreen(
                        startDestination = startDestination,
                        startTimer = {
                            startService(
                                Intent(
                                    this,
                                    TimerService::class.java,
                                ),
                            )
                        },
                        endTimer = {
                            stopService(
                                Intent(
                                    this,
                                    TimerService::class.java,
                                ),
                            )
                        },
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    TaskHiveTheme {
        MainScreen()
    }
}
