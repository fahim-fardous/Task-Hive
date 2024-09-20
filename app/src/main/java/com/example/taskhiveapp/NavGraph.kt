package com.example.taskhiveapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.taskhiveapp.presentation.analytics.AnalyticsScreen
import com.example.taskhiveapp.presentation.analytics.AnalyticsViewModel
import com.example.taskhiveapp.presentation.home.HomeScreen
import com.example.taskhiveapp.presentation.home.index.HomeIndexScreen
import com.example.taskhiveapp.presentation.log.list.LogListScreen
import com.example.taskhiveapp.presentation.log.list.LogListViewModel
import com.example.taskhiveapp.presentation.onboard.OnBoardScreen
import com.example.taskhiveapp.presentation.onboard.OnBoardViewModel
import com.example.taskhiveapp.presentation.settings.SettingsScreen
import com.example.taskhiveapp.presentation.settings.SettingsViewModel
import com.example.taskhiveapp.presentation.project.add.ProjectAddScreen
import com.example.taskhiveapp.presentation.project.add.ProjectAddViewModel
import com.example.taskhiveapp.presentation.task.add.TaskAddScreen
import com.example.taskhiveapp.presentation.task.add.TaskAddViewModel
import com.example.taskhiveapp.presentation.task.edit.TaskEditScreen
import com.example.taskhiveapp.presentation.task.edit.TaskEditViewModel
import com.example.taskhiveapp.presentation.task.list.TaskListScreen
import com.example.taskhiveapp.presentation.task.list.TaskListViewModel

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")

    data object OnBoard : Screen("onboard")

    data object Splash : Screen("splash")

    data object TaskList : Screen("task/list/{projectId}/{plannedDate}") {
        fun createRoute(
            projectId: Int? = null,
            plannedDate: Long? = null,
        ): String = "task/list/$projectId/$plannedDate"
    }

    data object TaskAdd : Screen("task/add/{projectId}") {
        fun createRoute(projectId: Int) = route.replaceFirst("{projectId}", "$projectId")
    }

    data object TaskEdit : Screen("task/edit/{taskId}") {
        fun createRoute(taskId: Int) = route.replaceFirst("{taskId}", "$taskId")
    }

    data object ProjectAdd : Screen("project/add")

    data object Analytics : Screen("analytics")

    data object Settings : Screen("settings")

    data object HomeIndex : Screen("home/index")

    data object LogList : Screen("log/list/{taskId}") {
        fun createRoute(taskId: Int) = route.replaceFirst("{taskId}", "$taskId")
    }

    data object LogAdd : Screen("log/list/add/{taskId}") {
        fun createRoute(taskId: Int) = route.replaceFirst("{taskId}", "$taskId")
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination) {
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
            val viewModel: OnBoardViewModel = hiltViewModel()
            OnBoardScreen(
                goToHome = {
                    navController.navigate(Screen.HomeIndex.route) {
                        popUpTo(Screen.OnBoard.route) {
                            inclusive = true
                        }
                    }
                },
                viewModel = viewModel,
            )
        }
        composable(
            route = Screen.TaskList.route,
            arguments =
                listOf(
                    navArgument("projectId") {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument("plannedDate") {
                        type = NavType.StringType
                        nullable = true
                    },
                ),
            deepLinks =
                listOf(
                    navDeepLink {
                        uriPattern = "taskhive://task/list/{projectId}/{plannedDate}"
                        action = android.content.Intent.ACTION_VIEW
                    },
                ),
        ) { backStackEntry ->
            val viewModel: TaskListViewModel = hiltViewModel()
            TaskListScreen(
                goBack = { navController.popBackStack() },
                goToAddTask = { projectId ->
                    navController.navigate(
                        Screen.TaskAdd.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
                goToEditTask = { taskId ->
                    navController.navigate(
                        Screen.TaskEdit.createRoute(taskId = taskId),
                    )
                },
                goToLogListScreen = { taskId ->
                    navController.navigate(
                        Screen.LogList.createRoute(taskId = taskId),
                    )
                },
                projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull(),
                plannedDate = backStackEntry.arguments?.getString("plannedDate")?.toLongOrNull(),
                viewModel = viewModel,
            )
        }

        composable(Screen.ProjectAdd.route) {
            val viewModel: ProjectAddViewModel = hiltViewModel()
            ProjectAddScreen(goBack = { navController.popBackStack() }, viewModel = viewModel)
        }
        composable(
            route = Screen.TaskAdd.route,
            arguments = listOf(navArgument("projectId") { type = NavType.IntType }),
        ) {
            val viewModel: TaskAddViewModel = hiltViewModel()
            TaskAddScreen(
                goBack = { navController.popBackStack() },
                projectId = navController.currentBackStackEntry?.arguments?.getInt("projectId")!!,
                viewModel = viewModel,
            )
        }
        composable(
            route = Screen.TaskEdit.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) {
            val viewModel: TaskEditViewModel = hiltViewModel()
            TaskEditScreen(
                goBack = { navController.popBackStack() },
                goToLogListScreen = { taskId ->
                    navController.navigate(
                        Screen.LogList.createRoute(taskId = taskId),
                    )
                },
                taskId = navController.currentBackStackEntry?.arguments?.getInt("taskId")!!,
                viewModel = viewModel,
            )
        }
        composable(Screen.Analytics.route) {
            val viewModel: AnalyticsViewModel = hiltViewModel()
            AnalyticsScreen(
                goBack = { navController.popBackStack() },
                viewModel = viewModel,
            )
        }
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel,
                goBack = { navController.popBackStack() },
            )
        }
        composable(Screen.HomeIndex.route) {
            HomeIndexScreen(
                goToAddProject = { navController.navigate(Screen.ProjectAdd.route) },
                goToTaskList = { projectId ->
                    navController.navigate(
                        Screen.TaskList.createRoute(projectId),
                    )
                },
                goToAddTask = { projectId ->
                    navController.navigate(
                        Screen.TaskAdd.createRoute(
                            projectId = projectId,
                        ),
                    )
                },
                goToEditTask = { taskId ->
                    navController.navigate(
                        Screen.TaskEdit.createRoute(taskId = taskId),
                    )
                },
                goToLogListScreen = { taskId ->
                    navController.navigate(
                        Screen.LogList.createRoute(taskId = taskId),
                    )
                },
            )
        }

        composable(
            Screen.LogList.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) {
            val viewModel: LogListViewModel = hiltViewModel()
            LogListScreen(
                taskId = navController.currentBackStackEntry?.arguments?.getInt("taskId")!!,
                goToAddLog = { taskId ->
                    navController.navigate(
                        Screen.LogAdd.createRoute(taskId),
                    )
                },
                goBack = { navController.popBackStack() },
                viewModel = viewModel,
            )
        }
    }
}