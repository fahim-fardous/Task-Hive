package com.example.taskhive.domain.model

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val logo: Int,
    val status: String,
    val time:String
)
