package com.example.taskhive.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.ui.theme.hintColor

@Composable
fun CardWithRow(
    icon: ImageVector,
    label: String,
    withCard: Boolean = false,
    iconColor: Color,
    borderColor:Color?
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = Color.White,
            ),
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (withCard) {
                Card(colors = CardDefaults.cardColors(containerColor = borderColor!!)) {
                    Icon(
                        icon,
                        contentDescription = "icon",
                        modifier =
                            Modifier
                                .size(30.dp)
                                .padding(4.dp),
                        tint = iconColor
                    )
                }
            } else {
                Icon(
                    icon,
                    contentDescription = "icon",
                    modifier =
                        Modifier
                            .size(30.dp),
                    tint = iconColor
                )
            }
            TextField(
                value = "",
                onValueChange = {},
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color =
                        hintColor,
                    )
                },
            )
        }
    }
}
