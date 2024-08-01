//package com.example.taskhive.presentation.splash
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.sp
//import com.example.taskhive.ui.theme.TaskHiveTheme
//import com.example.taskhive.ui.theme.appColor
//import kotlinx.coroutines.delay
//
//@Composable
//fun SplashScreen(
//    viewModel: SplashViewModel,
//    goToOnboardScreen: () -> Unit,
//    goToHomeScreen: () -> Unit,
//) {
//    LaunchedEffect(Unit) {
//        delay(3000L)
//        if (viewModel.isOnboardingCompleted()) {
//            goToHomeScreen()
//        } else {
//            goToOnboardScreen()
//        }
//    }
//    SplashScreenSkeleton()
//}
//
//@Composable
//fun SplashScreenSkeleton() {
//    Column(
//        modifier =
//            Modifier
//                .fillMaxSize()
//                .background(color = appColor),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Text(
//            modifier = Modifier.fillMaxWidth(),
//            text = "Task",
//            textAlign = TextAlign.Center,
//            fontSize = 32.sp,
//            fontWeight = FontWeight.SemiBold,
//            style =
//                MaterialTheme.typography.titleLarge.copy(
//                    brush =
//                        Brush.linearGradient(
//                            colors =
//                                listOf(
//                                    Color(0xFF020024),
//                                    Color(0xFFE5DEFD),
//                                    Color(0xFFEFB8C8),
//                                ),
//                        ),
//                ),
//        )
//        Text(
//            modifier = Modifier.fillMaxWidth(),
//            text = "Hive",
//            textAlign = TextAlign.Center,
//            fontSize = 48.sp,
//            fontWeight = FontWeight.SemiBold,
//            style =
//                MaterialTheme.typography.titleLarge.copy(
//                    brush =
//                        Brush.linearGradient(
//                            colors =
//                                listOf(
//                                    Color(0xFF020024),
//                                    Color(0xFFE5DEFD),
//                                    Color(0xFFEFB8C8),
//                                ),
//                        ),
//                ),
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun SplashScreenPreview() {
//    TaskHiveTheme {
//        SplashScreenSkeleton()
//    }
//}
