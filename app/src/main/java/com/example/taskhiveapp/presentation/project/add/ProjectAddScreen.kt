package com.example.taskhiveapp.presentation.project.add

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskhiveapp.components.CalendarPreferences
import com.example.taskhiveapp.components.CommonCard
import com.example.taskhiveapp.components.CommonDatePicker
import com.example.taskhiveapp.components.CustomButton
import com.example.taskhiveapp.components.SelectableColor
import com.example.taskhiveapp.components.SelectableIcon
import com.example.taskhiveapp.components.TopBar
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.ui.theme.TaskHiveTheme
import com.example.taskhiveapp.utils.SelectableProperties.backgroundColors
import com.example.taskhiveapp.utils.SelectableProperties.colors
import com.example.taskhiveapp.utils.SelectableProperties.icons
import com.example.taskhiveapp.utils.getReadableDate
import java.util.Date

@Composable
fun ProjectAddScreen(
    goBack: () -> Unit,
    viewModel: ProjectAddViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val showMessage by viewModel.showMessage.collectAsState()
    LaunchedEffect(showMessage) {
        if (showMessage != null) {
            Toast.makeText(context, showMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateMessage()
            if (showMessage == "Project saved") {
                goBack()
            }
        }
    }
    ProjectAddScreenSkeleton(
        goBack = goBack,
        saveProject = { project ->
            viewModel.saveProject(project)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectAddScreenSkeleton(
    goBack: () -> Unit = {},
    saveProject: (Project) -> Unit = { _ -> },
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var selectedIcon by remember { mutableIntStateOf(0) }
    var selectedColor by remember { mutableIntStateOf(0) }
    var selectedBorderColor by remember { mutableIntStateOf(0) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
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
                modifier = Modifier.padding(16.dp),
                onClick =
                    {
                        saveProject(
                            Project(
                                name = name,
                                description = description,
                                selectedIcon = selectedIcon,
                                selectedIconColor = selectedColor,
                                selectedBorderColor = selectedBorderColor,
                                endDate = Date(System.currentTimeMillis() + 86400000),
                            ),
                        )
                        CalendarPreferences(context = context).clearSelectedDate()
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
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = "Project Name",
                lines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = "Description",
                lines = 5,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CommonCard(
                    modifier = Modifier.weight(1f),
                    value = endDate.getReadableDate(),
                    onValueChange = { },
                    label = "End Date",
                    lines = 1,
                    readOnly = true,
                )
                FloatingActionButton(onClick = { showDatePickerDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add time")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Select an icon",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
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
            Text(
                text = "Select icon color",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
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
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(backgroundColors) { color ->
                    SelectableColor(
                        onClick = { selectedBorderColor = backgroundColors.indexOf(color) },
                        color = color,
                        isSelected = selectedBorderColor == backgroundColors.indexOf(color),
                    )
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------
    // Dialog
    // -----------------------------------------------------------------------------------

    if (showDatePickerDialog) {
        CommonDatePicker(title = "Select End Date", onDateSelected = {
            endDate = Date(it)
        }, onDismiss = { showDatePickerDialog = false })
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskAddScreenSkeletonPreview() {
    TaskHiveTheme {
        ProjectAddScreenSkeleton()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenSkeletonPreviewDark() {
    TaskHiveTheme {
        ProjectAddScreenSkeleton()
    }
}
