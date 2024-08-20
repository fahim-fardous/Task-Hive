package com.example.taskhive.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteAlertDialog(
    showDeleteDialog: (Boolean) -> Unit = {},
    title: String,
    onDeleteClicked: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    AlertDialog(
        text = { Text(text = title) },
        onDismissRequest = {
            showDialog = false
            showDeleteDialog(false)
        },
        confirmButton = {
            TextButton(onClick = {
                showDialog = false
                showDeleteDialog(false)
                onDeleteClicked()
            }) {
                Text(text = "Delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showDialog = false
                showDeleteDialog(false)
            }) {
                Text(text = "Cancel")
            }
        },
    )
}
