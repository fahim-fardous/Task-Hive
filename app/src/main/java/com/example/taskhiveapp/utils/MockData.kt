package com.example.taskhiveapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import com.example.taskhiveapp.domain.model.Log
import com.example.taskhiveapp.domain.model.MockModel
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.domain.model.TaskStatus
import com.example.taskhiveapp.domain.model.toUiModel
import java.time.LocalDate
import java.util.Date

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

    val taskIcon =
        mapOf(
            "Office Project" to Icons.Filled.Backpack,
            "Personal Project" to Icons.Filled.Person,
            "Daily Study" to Icons.Filled.Book,
        )
    val project =
        Project(
            id = 1,
            name = "Project 1",
            description = "This is a sample project",
            endDate = Date(),
            selectedIcon = 0,
            selectedIconColor = 0xFF000000.toInt(),
            selectedBorderColor = 0xFF000000.toInt(),
        )
    val task =
        Task(
            id = 1,
            title = "Sample Task",
            description = "This is a sample task description",
            plannedStartTime = Date(),
            plannedEndTime = Date(),
            actualStartTime = Date(),
            actualEndTime = Date(),
            totalTimeSpend = 3600L,
            project = project,
            taskStatus = TaskStatus.IN_PROGRESS,
        ).toUiModel()

    val log =
        Log(
            id = 1,
            startTime = Date(),
            endTime = Date(),
            duration = 3600L,
            taskId = 1,
            startDate = localDateToDate(LocalDate.now()),
            endDate = localDateToDate(LocalDate.now()),
        )
}
