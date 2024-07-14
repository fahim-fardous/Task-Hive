package com.example.taskhive

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.presentation.home.index.HomeIndexScreen
import com.example.taskhive.presentation.notes.NoteScreen
import com.example.taskhive.presentation.onboard.OnBoardScreen
import com.example.taskhive.presentation.profile.ProfileScreen
import com.example.taskhive.presentation.project.add.ProjectAddScreen
import com.example.taskhive.presentation.task.add.TaskAddScreen
import com.example.taskhive.presentation.task.list.TaskListScreen

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")

    data object OnBoard : Screen("onboard")

    data object TaskList : Screen("task/list/{projectId}") {
        fun createRoute(projectId: Int?) = route.replaceFirst("{projectId}", "$projectId")
    }

    data object TaskAdd : Screen("task/add/{projectId}") {
        fun createRoute(projectId: Int) = route.replaceFirst("{projectId}", "$projectId")
    }

    data object ProjectAdd : Screen("project/add")

    data object Notes : Screen("notes")

    data object Profile : Screen("profile")

    data object HomeIndex : Screen("home/index")
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.OnBoard.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                goToAddProject = { navController.navigate(Screen.ProjectAdd.route) },
                goToTaskList = { projectId ->
                    navController.navigate(
                        Screen.TaskList.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
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
        composable(
            route = Screen.TaskList.route,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType }),
        ) { backStackEntry ->
            TaskListScreen(
                goBack = { navController.popBackStack() },
                goToAddTask = { projectId ->
                    navController.navigate(
                        Screen.TaskAdd.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
                projectId = backStackEntry.arguments?.getInt("projectId"),
            )
        }
        composable(Screen.ProjectAdd.route) {
            ProjectAddScreen { navController.popBackStack() }
        }
        composable(
            route = Screen.TaskAdd.route,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType }),
        ) {
            TaskAddScreen(
                goBack = { navController.popBackStack() },
                projectId = navController.currentBackStackEntry?.arguments?.getInt("projectId")!!,
            )
        }
        composable(Screen.Notes.route) {
            NoteScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.HomeIndex.route) {
            HomeIndexScreen(
                goToAddProject = { navController.navigate(Screen.ProjectAdd.route) },
                goToTaskList = { projectId ->
                    navController.navigate(
                        Screen.TaskList.createRoute(projectId),
                    )
                },
            )
        }
    }
}
