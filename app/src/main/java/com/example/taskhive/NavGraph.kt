package com.example.taskhive

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.presentation.home.index.HomeIndexScreen
import com.example.taskhive.presentation.notes.NoteScreen
import com.example.taskhive.presentation.onboard.OnBoardScreen
import com.example.taskhive.presentation.profile.ProfileScreen
import com.example.taskhive.presentation.task.add.TaskAddScreen
import com.example.taskhive.presentation.task.list.TaskListScreen

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")

    data object OnBoard : Screen("onboard")

    data object TaskList : Screen("task/list")

    data object TaskAdd : Screen("task/add")

    data object Notes : Screen("notes")

    data object Profile : Screen("profile")

    data object HomeIndex : Screen("home/index")
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.OnBoard.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                goToAddTask = { navController.navigate(Screen.TaskAdd.route) },
            )
        }
        composable(Screen.OnBoard.route) {
            OnBoardScreen(
                goToHome = {
                    navController.navigate(Screen.HomeIndex.route) {
                        popUpTo(Screen.OnBoard.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable(Screen.TaskList.route) {
            TaskListScreen(
                goBack = { navController.popBackStack() },
            )
        }
        composable(Screen.TaskAdd.route) {
            TaskAddScreen { navController.popBackStack() }
        }
        composable(Screen.Notes.route) {
            NoteScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.HomeIndex.route) {
            HomeIndexScreen(goToAddTask = { navController.navigate(Screen.TaskAdd.route) })
        }
    }
}
