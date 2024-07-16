package com.example.taskhive.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskhive.ui.theme.hintColor

@Composable
fun CommonCard(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    lines: Int = 1,
    height: Dp = 200.dp,
    readOnly: Boolean = false,
) {
    println("It is $value value")
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier =
        modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = Color.White,
            ),
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            modifier =
            Modifier
                .fillMaxWidth()
                .then(
                    if (lines > 1) Modifier.height(height) else Modifier,
                ),
            textStyle =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                ),
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = hintColor,
                )
            },
            maxLines = lines,
            readOnly = readOnly,
        )
    }
}
