package com.example.taskhiveapp.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhiveapp.ui.theme.TaskHiveTheme
import com.example.taskhiveapp.ui.theme.appColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    title: String,
    trailingIcon: ImageVector? = null,
    goToLogListScreen: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
) {
    var showMoreMenu by remember {
        mutableStateOf(false)
    }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        actions = {
            if (trailingIcon != null) {
                if (trailingIcon == Icons.Filled.Notifications) {
                    BadgedBox(modifier = Modifier.padding(end = 8.dp), badge = {
                        Badge(
                            containerColor = appColor,
                            modifier = Modifier.size(10.dp),
                        )
                    }) {
                        Icon(
                            trailingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier =
                                Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        onNotificationClick()
                                    },
                        )
                    }
                } else {
                    Icon(
                        trailingIcon,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier =
                            Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    showMoreMenu = true
                                },
                    )
                    // Dropdown Menu
                    if (showMoreMenu) {
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false },
                        ) {
                            DropdownMenuItem(text = { Text(text = "Show logs") }, onClick = {
                                showMoreMenu = false
                                goToLogListScreen()
                            })
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    TaskHiveTheme {
        TopBar(
            onClick = {},
            leadingIcon = Icons.Filled.Notifications,
            title = "TaskHive",
            trailingIcon = Icons.Filled.Notifications,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPreviewDark() {
    TaskHiveTheme {
        TopBar(
            onClick = {},
            leadingIcon = Icons.Filled.Notifications,
            title = "TaskHive",
            trailingIcon = Icons.Filled.Notifications,
        )
    }
}
