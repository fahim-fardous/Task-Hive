package com.example.taskhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskhive.domain.model.TaskStatus

@Composable
fun TaskStatusDialog(
    currentStatus: TaskStatus,
    onDismiss: () -> Unit,
    onSave: (TaskStatus) -> Unit,
) {
    var status by remember { mutableStateOf(currentStatus) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Status") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = status == TaskStatus.TODO,
                        onClick = { status = TaskStatus.TODO },
                    )
                    Text(text = "To do")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = status == TaskStatus.IN_PROGRESS,
                        onClick = { status = TaskStatus.IN_PROGRESS },
                    )
                    Text(text = "In Progress")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = status == TaskStatus.DONE,
                        onClick = { status = TaskStatus.DONE },
                    )
                    Text(text = "Done")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(status)
                },
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun DataInputDialogPreview() {
    TaskStatusDialog(currentStatus = TaskStatus.TODO, onDismiss = { /*TODO*/ }) {
    }
}
