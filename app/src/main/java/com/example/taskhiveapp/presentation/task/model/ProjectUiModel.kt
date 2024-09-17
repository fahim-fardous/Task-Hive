package com.example.taskhiveapp.presentation.task.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.Date

data class ProjectUiModel(
    val id: Int = 0,
    val name: String,
    val description: String,
    val endDate: Date,
    val progress: Float = 0.0f,
    val numberOfTask: Int = 0,
    val selectedIcon: Int,
    val selectedIconColor: Int,
    val selectedBorderColor: Int,
    var isSelected: MutableState<Boolean> = mutableStateOf(false),
)
