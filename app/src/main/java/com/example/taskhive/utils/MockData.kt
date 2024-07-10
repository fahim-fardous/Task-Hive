package com.example.taskhive.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import com.example.taskhive.domain.model.MockModel

object MockData {
    val tasks =
        listOf(
            MockModel(
                taskGroup = "Office Project",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "Done",
            ),
            MockModel(
                taskGroup = "Personal Project",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "To-do",
            ),
            MockModel(
                taskGroup = "Daily Study",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "In Progress",
            ),
            MockModel(
                taskGroup = "Office Project",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "Done",
            ),
            MockModel(
                taskGroup = "Office Project",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "Done",
            ),
            MockModel(
                taskGroup = "Personal Project",
                projectName = "Task Management and To do app design",
                taskName = "Market Research",
                endTime = "10:00 AM",
                status = "To-do",
            ),
        )
    val taskGroupIconColor =
        mapOf(
            "Office Project" to Color(0xFFF378B7),
            "Personal Project" to
                Color(0xFF9160F3),
            "Daily Study" to Color(0xFFFE9042),
        )
    val taskGroupBackgroundColor =
        mapOf(
            "Office Project" to Color(0xFFFDE2F1),
            "Personal Project" to
                Color(0xFFECE3FE),
            "Daily Study" to Color(0xFFFEE5D3),
        )

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

    val taskIcon = mapOf(
        "Office Project" to Icons.Filled.Backpack,
        "Personal Project" to Icons.Filled.Person,
        "Daily Study" to Icons.Filled.Book
    )
}
