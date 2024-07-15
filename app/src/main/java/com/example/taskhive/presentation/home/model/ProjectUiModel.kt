package com.example.taskhive.presentation.home.model

import java.util.Date

data class ProjectUiModel(
    val id: Int,
    val name: String,
    val description: String,
    val endDate: Date,
    val selectedIcon: Int,
    val selectedIconColor: Int,
    val selectedBorderColor: Int,
    val numberOfTask: Int = 0,
    val progress: Float = 0.0f,
)
