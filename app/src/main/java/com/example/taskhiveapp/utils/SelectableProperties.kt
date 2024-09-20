package com.example.taskhiveapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color

object SelectableProperties {
    val icons =
        listOf(
            Icons.Filled.Backpack,
            Icons.Filled.Person,
            Icons.Filled.Book,
            Icons.Filled.Home,
            Icons.Filled.ShoppingCart,
        )
    val colors =
        listOf(
            Color(0xFFF378B7),
            Color(0xFF9160F3),
            Color(0xFFFE9042),
            Color.Green,
            Color.Magenta,
        )
    val progressIndicatorColors =
        listOf(
            Color(0xFFF378B7),
            Color(0xFF9160F3),
            Color(0xFFFE9042),
            Color.Green,
            Color.Magenta,
        )
    val backgroundColors =
        listOf(
            Color(0xFFFDE2F1),
            Color(0xFFECE3FE),
            Color(0xFFFEE5D3),
            Color(0xFFD7FDD9),
            Color(0xFFFCE4EC),
        )
}
