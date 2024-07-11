package com.example.taskhive.presentation.project.add

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.SelectableColor
import com.example.taskhive.components.SelectableIcon
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Project
import com.example.taskhive.ui.theme.TaskHiveTheme

@Composable
fun ProjectAddScreen(goBack: () -> Unit) {
    val viewModel: ProjectAddViewModel = viewModel()
    val showMessage by viewModel.showMessage.collectAsState()
    LaunchedEffect(showMessage) {
        if (showMessage != null) {
            if (showMessage == "Project saved") {
                goBack()
            }
        }
    }
    ProjectAddScreenSkeleton(
        goBack = goBack,
        saveProject = { project, context ->
            viewModel.saveProject(project, context)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskAddScreenSkeletonPreview() {
    TaskHiveTheme {
        ProjectAddScreenSkeleton()
    }
}

@Composable
fun ProjectAddScreenSkeleton(
    goBack: () -> Unit = {},
    saveProject: (Project, Context) -> Unit = { _, _ -> },
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    val icons =
        listOf(
            Icons.Filled.Backpack,
            Icons.Filled.Person,
            Icons.Filled.Book,
            Icons.Filled.Home,
            Icons.Filled.ShoppingCart,
        )
    val colors =
        listOf(
            Color(0xFFF378B7),
            Color(0xFF9160F3),
            Color(0xFFFE9042),
            Color.Green,
            Color.Magenta,
        )
    val borderColors =
        listOf(
            Color(0xFFFDE2F1),
            Color(0xFFECE3FE),
            Color(0xFFFEE5D3),
            Color(0xFFD7FDD9),
            Color(0xFFFCE4EC),
        )
    var selectedIcon by remember { mutableIntStateOf(0) }
    var selectedColor by remember { mutableIntStateOf(0) }
    var selectedBorderColor by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopBar(
                onClick = { goBack() },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                title = "Add Project",
                trailingIcon = Icons.Filled.Notifications,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomButton(
                onClick =
                    {
                    },
                text = "Add Project",
                trailingIcon = null,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            CommonCard(
                value = name,
                onValueChange = { name = it },
                label = "Project Name",
                lines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            CommonCard(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                lines = 5,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Select an icon", color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(icons) { icon ->
                    SelectableIcon(
                        onClick = { selectedIcon = icons.indexOf(icon) },
                        icon = icon,
                        isSelected = selectedIcon == icons.indexOf(icon),
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Select icon color", color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(colors) { color ->
                    SelectableColor(
                        onClick = { selectedColor = colors.indexOf(color) },
                        color = color,
                        isSelected = selectedColor == colors.indexOf(color),
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Select icon background color",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(borderColors) { color ->
                    SelectableColor(
                        onClick = { selectedBorderColor = borderColors.indexOf(color) },
                        color = color,
                        isSelected = selectedBorderColor == borderColors.indexOf(color),
                    )
                }
            }
        }
    }
}
