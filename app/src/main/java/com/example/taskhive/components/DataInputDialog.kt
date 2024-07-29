package com.example.taskhive.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DataInputDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var inputData by remember { mutableStateOf("") }
    var errorMessage by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("What have you done?") },
        text = {
            Column {
                TextField(
                    value = inputData,
                    onValueChange = { inputData = it },
                    placeholder = { Text("Title") },
                    supportingText = {
                        Text(text = errorMessage, color = Color.Red)
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (inputData.isNotBlank()) {
                        onSave(inputData)
                    }
                    else{
                        errorMessage = "Title can't be empty"
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                enabled = inputData.isNotBlank()
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
private fun DataInputDialogPreview() {
    DataInputDialog(onDismiss = { /*TODO*/ }) {

    }
}