package com.example.taskhive.presentation.task.model

import java.util.Date

data class ProjectUiModel(
    val id: Int = 0,
    val name: String,
    val description: String,
    val endDate: Date,
    val numberOfTask: Int = 0,
    val selectedIcon: Int,
    val selectedIconColor: Int,
    val selectedBorderColor: Int,
)