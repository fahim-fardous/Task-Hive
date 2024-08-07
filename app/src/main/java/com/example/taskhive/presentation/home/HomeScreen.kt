package com.example.taskhive.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.R
import com.example.taskhive.components.InProgressCard
import com.example.taskhive.components.ProgressCard
import com.example.taskhive.components.TaskGroup
import com.example.taskhive.presentation.task.model.ProjectUiModel
import com.example.taskhive.presentation.task.model.TaskUiModel
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.ui.theme.appColor
import com.example.taskhive.utils.SelectableProperties.backgroundColors
import com.example.taskhive.utils.SelectableProperties.colors
import com.example.taskhive.utils.SelectableProperties.icons

@Composable
fun HomeScreen(
    goToAddProject: () -> Unit,
    goToTaskList: (Int?) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getProjects()
    }
    LaunchedEffect(viewModel.count) {
        viewModel.getNumberOfProject()
    }
    LaunchedEffect(Unit) {
        viewModel.getInProgressTasks()
    }
    val projects by viewModel.projects.collectAsState()
    val inProgressTasks by viewModel.inProgressTasks.collectAsState()
    val numberOfProject by viewModel.count.collectAsState()
    HomeScreenSkeleton(
        goToAddProject = goToAddProject,
        projects = projects,
        inProgressTasks = inProgressTasks,
        numberOfProject = numberOfProject,
        goToTaskList = { projectId ->
            goToTaskList(projectId)
        },
    )
}

@Composable
fun HomeScreenSkeleton(
    goToAddProject: () -> Unit = {},
    projects: List<ProjectUiModel> = emptyList(),
    inProgressTasks: List<TaskUiModel> = emptyList(),
    numberOfProject: Int = 0,
    goToTaskList: (Int?) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.my_img),
                    contentDescription = "my photo",
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(
                                CircleShape,
                            ),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier =
                        Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Hello!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Fahim",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                BadgedBox(modifier = Modifier.padding(end = 8.dp), badge = {
                    Badge(
                        containerColor = appColor,
                        modifier = Modifier.size(10.dp),
                    )
                }) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { goToAddProject() },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            ProgressCard(onClick = { /*TODO*/ }, progress = 0.85f)
            if (inProgressTasks.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "In Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 16.dp, start = 8.dp)
                                .size(24.dp)
                                .background(
                                    color = Color(0xFFEDE8FE),
                                    shape = CircleShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = inProgressTasks.size.toString(),
                            color = appColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                LazyRow(
                    contentPadding =
                        PaddingValues(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 0.dp,
                            end = 0.dp,
                        ),
                ) {
                    items(inProgressTasks) { task ->
                        InProgressCard(
                            projectName = task.project.name,
                            taskName = task.title,
                            progress = 0.5f,
                            projectId = task.project.id,
                            icon = icons[task.project.selectedIcon],
                            iconColor = colors[task.project.selectedIconColor],
                            borderColor = backgroundColors[task.project.selectedBorderColor],
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Projects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Box(
                    modifier =
                        Modifier
                            .padding(top = 16.dp, start = 8.dp)
                            .size(24.dp)
                            .background(
                                color = Color(0xFFEDE8FE),
                                shape = CircleShape,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = numberOfProject.toString(),
                        color = appColor,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            LazyColumn(
                contentPadding =
                    PaddingValues(
                        start = 0.dp,
                        end = 0.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                    ),
            ) {
                items(projects) { project ->
                    TaskGroup(
                        onClick = {
                            goToTaskList(project.id)
                        },
                        project = project.name,
                        numberOfTask = project.numberOfTask,
                        progress = if (project.progress.isNaN()) 0.0f else project.progress,
                        selectedIcon = project.selectedIcon,
                        selectedIconColor = project.selectedIconColor,
                        selectedBorderColor = project.selectedBorderColor,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenSkeletonPreview() {
    TaskHiveTheme {
        HomeScreenSkeleton()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        HomeScreenSkeleton()
    }
}
