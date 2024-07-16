package com.example.taskhive.utils

import androidx.compose.ui.graphics.Color

object MockData {
    val statusTextColor =
        mapOf(
            "Done" to Color(0xFF9160F3),
            "To-do" to
                    Color(0xFF0086FE),
            "In Progress" to Color(0xFFFE9042),
        )
    val statusBackgroundColor =
        mapOf(
            "Done" to Color(0xFFECE3FE),
            "To-do" to
                    Color(0xFFE6F2FE),
            "In Progress" to Color(0xFFFEE5D3),
        )
}