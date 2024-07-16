package com.example.taskhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskhive.ui.theme.appColor

@Composable
fun CustomButton(
    onClick: () -> Unit = {},
    text: String,
    trailingIcon: ImageVector? = null,
    textColor: Color = Color.White,
    backgroundColor: Color = appColor,
    marginStart: Dp = 0.dp
) {
    Box(
        modifier =
        Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(fraction = 1.0f)
            .background(color = appColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp).clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(start = marginStart),
                textAlign = TextAlign.Center,
            )
            if (trailingIcon != null) {
                Icon(
                    trailingIcon,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomButtonPreview() {
    Column {
        CustomButton(text = "Let's Start", trailingIcon = Icons.AutoMirrored.Filled.ArrowForward)
        CustomButton(text = "Let's Start", trailingIcon = null)
    }
}
