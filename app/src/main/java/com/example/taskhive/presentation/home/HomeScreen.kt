package com.example.taskhive.presentation.home

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
import androidx.compose.material.icons.filled.Backpack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.R
import com.example.taskhive.components.InProgressCard
import com.example.taskhive.components.ProgressCard
import com.example.taskhive.components.TaskGroup
import com.example.taskhive.domain.model.Project
import com.example.taskhive.ui.theme.appColor

@Composable
fun HomeScreen(
    goToAddProject: () -> Unit,
    goToTaskList: (Int?) -> Unit = {},
) {
    val viewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getProjects(context)
    }
    LaunchedEffect(viewModel.count) {
        viewModel.getNumberOfProject(context)
    }
    val projects by viewModel.projects.collectAsState()
    val numberOfProject by viewModel.count.collectAsState()
    HomeScreenSkeleton(
        goToAddProject = goToAddProject,
        projects = projects,
        numberOfProject = numberOfProject,
    )
}

@Composable
fun HomeScreenSkeleton(
    goToAddProject: () -> Unit = {},
    projects: List<Project> = emptyList(),
    numberOfProject: Int = 0,
) {
    val context = LocalContext.current
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
                    Text(text = "Hello!", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Fahim",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
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
                        tint = Color.Black,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { goToAddProject() },
                containerColor = appColor,
                shape = CircleShape,
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.White,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "In Progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
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
                        text = "5",
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
                items(5) { id ->
                    InProgressCard(
                        taskGroup = "Office Project",
                        projectName = "Grocery shopping app design",
                        progress = 0.85f,
                        id = id,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Projects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
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
                        project = project.name,
                        numberOfTask = 0,
                        progress = 0.0f,
                        selectedIcon = 0,
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
    HomeScreenSkeleton()
}
