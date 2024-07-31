package com.example.taskhive.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties

@Composable
fun IconWithDropdownMenu(
    onDeleteClicked: () -> Unit = {},
    onChangeStatusClicked: () -> Unit = {},
    onShowLogsClicked: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Show options",
            modifier = Modifier.clickable { expanded = true },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(),
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteClicked()
            }, text = { Text(text = "Delete") })
            DropdownMenuItem(onClick = {
                expanded = false
                onChangeStatusClicked()
            }, text = { Text(text = "Change status") })
            DropdownMenuItem(onClick = {
                expanded = false
                onShowLogsClicked()
            }, text = { Text(text = "Show logs") })
        }
    }
}

@Preview
@Composable
fun PreviewIconWithDropdownMenu() {
    IconWithDropdownMenu()
}
