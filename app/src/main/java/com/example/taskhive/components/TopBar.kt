package com.example.taskhive.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskhive.ui.theme.appColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    title: String,
    trailingIcon: ImageVector,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.Black,
                )
            }
        },
        actions = {
            BadgedBox(modifier = Modifier.padding(end = 8.dp), badge = {
                Badge(
                    containerColor = appColor,
                    modifier = Modifier.size(10.dp),
                )
            }) {
                Icon(
                    trailingIcon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        },
    )
}
