package com.example.taskhive.presentation.home.index

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskhive.presentation.home.HomeScreen
import com.example.taskhive.presentation.notes.NoteScreen
import com.example.taskhive.presentation.profile.ProfileScreen
import com.example.taskhive.presentation.task.list.TaskListScreen

@Composable
fun HomeIndexScreen(goToAddTask: () -> Unit) {
    val navController = rememberNavController()
    HomeIndexScreenSkeleton(navController = navController, goToAddTask = goToAddTask)
}

@Composable
fun HomeIndexScreenSkeleton(
    navController: NavHostController,
    goToAddTask: () -> Unit = {},
) {
    val items =
        listOf(
            HomeTabScreen.Home,
            HomeTabScreen.TaskList,
            HomeTabScreen.Notes,
            HomeTabScreen.Profile,
        )

    Scaffold(
        Modifier,
        bottomBar = {
            NavigationBar() {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(text = screen.title, textAlign = TextAlign.Center)
                        },
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeTabScreen.Home.route,
            Modifier.padding(innerPadding),
        ) {
            composable(HomeTabScreen.Home.route) {
                HomeScreen(
                    goToAddTask = goToAddTask,
                )
            }
            composable(HomeTabScreen.TaskList.route) {
                TaskListScreen(goBack = { navController.popBackStack() })
            }
            composable(HomeTabScreen.Notes.route) {
                NoteScreen()
            }
            composable(HomeTabScreen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Preview
@Composable
private fun HomeIndexScreenSkeletonPreview() {
    HomeIndexScreenSkeleton(navController = rememberNavController())
}

// --------------------------------------
// Tab Screens
// --------------------------------------

private sealed class HomeTabScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Home : HomeTabScreen("home_screen", "Home", Icons.Filled.Home)

    data object TaskList : HomeTabScreen("task_list_screen", "Task List", Icons.Filled.CalendarMonth)

    data object Notes : HomeTabScreen("notes_screen", "Notes", Icons.AutoMirrored.Filled.Note)

    data object Profile : HomeTabScreen("profile_screen", "Profile", Icons.Filled.Person)
}
