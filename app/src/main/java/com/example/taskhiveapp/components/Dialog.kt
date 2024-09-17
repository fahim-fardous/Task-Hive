package com.example.taskhiveapp.components

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
fun Dialog(
    showDialog: (Boolean) -> Unit = {},
    title: String,
    confirmText: String = "Delete",
    onClicked: () -> Unit,
) {
    var showAlertDialog by remember { mutableStateOf(false) }
    AlertDialog(
        text = { Text(text = title) },
        onDismissRequest = {
            showAlertDialog = false
            showDialog(false)
        },
        confirmButton = {
            TextButton(onClick = {
                showAlertDialog = false
                showDialog(false)
                onClicked()
            }) {
                Text(text = confirmText, color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showAlertDialog = false
                showDialog(false)
            }) {
                Text(text = "Cancel")
            }
        },
    )
}
