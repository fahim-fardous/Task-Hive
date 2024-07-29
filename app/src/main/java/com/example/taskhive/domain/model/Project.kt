package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskhive.presentation.task.model.ProjectUiModel
import java.util.Date

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val endDate: Date,
    val selectedIcon: Int,
    val selectedIconColor: Int,
    val selectedBorderColor: Int,
)

fun Project.toUiModel() = ProjectUiModel(
    id = id,
    name = name,
    description = description,
    endDate = endDate,
    selectedIcon = selectedIcon,
    selectedIconColor = selectedIconColor,
    selectedBorderColor = selectedBorderColor
)