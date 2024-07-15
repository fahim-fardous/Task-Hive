package com.example.taskhive

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.presentation.home.HomeViewModel
import com.example.taskhive.presentation.home.index.HomeIndexScreen
import com.example.taskhive.presentation.notes.NoteScreen
import com.example.taskhive.presentation.onboard.OnBoardScreen
import com.example.taskhive.presentation.profile.ProfileScreen
import com.example.taskhive.presentation.project.add.ProjectAddScreen
import com.example.taskhive.presentation.project.add.ProjectAddViewModel
import com.example.taskhive.presentation.task.add.TaskAddScreen
import com.example.taskhive.presentation.task.add.TaskAddViewModel
import com.example.taskhive.presentation.task.edit.TaskEditScreen
import com.example.taskhive.presentation.task.edit.TaskEditViewModel
import com.example.taskhive.presentation.task.list.TaskListScreen
import com.example.taskhive.presentation.task.list.TaskListViewModel

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

    data object TaskEdit: Screen("task/edit/{taskId}") {
        fun createRoute(taskId: Int) = route.replaceFirst("{taskId}", "$taskId")
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
            val viewModel:HomeViewModel = hiltViewModel()
            HomeScreen(
                goToAddProject = { navController.navigate(Screen.ProjectAdd.route) },
                goToTaskList = { projectId ->
                    navController.navigate(
                        Screen.TaskList.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
                viewModel = viewModel
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
        composable(Screen.ProjectAdd.route) {
            val viewModel:ProjectAddViewModel = hiltViewModel()
            ProjectAddScreen(goBack = {navController.popBackStack()}, viewModel = viewModel)
        }
        composable(
            route = Screen.TaskList.route,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val viewModel:TaskListViewModel = hiltViewModel()
            TaskListScreen(
                goBack = { navController.popBackStack() },
                goToAddTask = { projectId ->
                    navController.navigate(
                        Screen.TaskAdd.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
                goToTaskEdit = { taskId ->
                    navController.navigate(Screen.TaskEdit.createRoute(taskId = taskId))
                },
                projectId = backStackEntry.arguments?.getInt("projectId"),
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.TaskAdd.route,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType }),
        ) {
            val viewModel:TaskAddViewModel = hiltViewModel()
            TaskAddScreen(
                goBack = { navController.popBackStack() },
                projectId = navController.currentBackStackEntry?.arguments?.getInt("projectId")!!,
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.TaskEdit.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val viewModel:TaskEditViewModel = hiltViewModel()
            TaskEditScreen(
                goBack = { navController.popBackStack() },
                taskId = backStackEntry.arguments?.getInt("taskId")!!,
                viewModel = viewModel
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
