package com.example.taskhive

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.presentation.notes.NoteScreen
import com.example.taskhive.presentation.onboard.OnBoardScreen
import com.example.taskhive.presentation.profile.ProfileScreen
import com.example.taskhive.presentation.task.add.TaskAddScreen
import com.example.taskhive.presentation.task.list.TaskListScreen

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home_screen")

    data object OnBoard : Screen("onboard_screen")

    data object TaskList : Screen("task_list_screen")

    data object TaskAdd : Screen("task_add_screen")

    data object Notes : Screen("notes_screen")

    data object Profile : Screen("profile_screen")
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                goToAddTask = {navController.navigate(Screen.TaskAdd.route)}
            )
        }
        composable(Screen.OnBoard.route) {
            OnBoardScreen()
        }
        composable(Screen.TaskList.route) {
            TaskListScreen()
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
    }
}
