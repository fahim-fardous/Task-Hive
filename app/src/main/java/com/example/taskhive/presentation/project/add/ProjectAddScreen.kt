package com.example.taskhive.presentation.project.add

import android.content.res.Configuration
import android.icu.util.Calendar
import android.icu.util.TimeZone
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.taskhive.components.CommonCard
import com.example.taskhive.components.CustomButton
import com.example.taskhive.components.SelectableColor
import com.example.taskhive.components.SelectableIcon
import com.example.taskhive.components.TopBar
import com.example.taskhive.domain.model.Project
import com.example.taskhive.ui.theme.TaskHiveTheme
import com.example.taskhive.utils.SelectableProperties.backgroundColors
import com.example.taskhive.utils.SelectableProperties.colors
import com.example.taskhive.utils.SelectableProperties.icons
import com.example.taskhive.utils.getReadableDate
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
                    label = "End Time",
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
        val initialSelectedDate =
            remember {
                val localCalender = Calendar.getInstance()
                val utcCalender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utcCalender.clear()
                utcCalender.set(
                    localCalender.get(Calendar.YEAR),
                    localCalender.get(Calendar.MONTH),
                    localCalender.get(Calendar.DATE),
                )
                utcCalender.timeInMillis
            }

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = initialSelectedDate,
                selectableDates =
                    object : SelectableDates {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

                        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= calendar.timeInMillis

                        override fun isSelectableYear(year: Int): Boolean = year >= calendar.get(Calendar.YEAR)
                    },
            )
        val datePickerConfirmButtonEnabled =
            remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }

        DatePickerDialog(onDismissRequest = { showDatePickerDialog = false }, confirmButton = {
            TextButton(
                onClick = {
                    showDatePickerDialog = false

                    datePickerState.selectedDateMillis?.let {
                        endDate = Date(it)
                    }
                },
                enabled = datePickerConfirmButtonEnabled.value,
            ) {
                Text(text = "OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePickerDialog = false }) {
                Text(text = "Cancel")
            }
        }) {
            DatePicker(state = datePickerState, title = {
                Text(
                    text = "Task Date",
                    Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                )
            })
        }
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
