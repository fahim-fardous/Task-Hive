package com.example.taskhive.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val appColor = Color(0xFF5F33E1)
var hintColor = Color(0xFF6E6A7C)
var secondaryColor = Color(0xFFE5DEFD)

sealed class ThemeColors(
    val background:Color,
    val textColor:Color,
    val iconColor:Color
){
    data object Day:ThemeColors(
        background = Color.White,
        iconColor = Color.Black,
        textColor = Color.Black,
    )
    data object Night:ThemeColors(
        background = Color.Black,
        iconColor = Color.White,
        textColor = Color.White,
    )
}