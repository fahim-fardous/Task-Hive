package com.example.taskhiveapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.taskhiveapp.presentation.task.model.ProjectUiModel

@Composable
fun SelectProjectsDialog(
    projects: List<ProjectUiModel>,
    onDismissRequest: () -> Unit,
    onConfirm: (List<ProjectUiModel>) -> Unit,
) {
    val selectedProjects = mutableListOf<ProjectUiModel>()
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = {
                for (project in projects) {
                    if (project.isSelected.value) {
                        selectedProjects.add(project)
                    }
                }
                onConfirm(selectedProjects)
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = "Cancel")
            }
        },
        title = { Text(text = "Select projects to export") },
        text = {
            LazyColumn {
                items(projects) { project ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = project.isSelected.value,
                            onCheckedChange = { isChecked ->
                                project.isSelected.value = isChecked
                            },
                        )

                        Text(
                            text = project.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        )
                    }
                }
            }
        },
    )
}
