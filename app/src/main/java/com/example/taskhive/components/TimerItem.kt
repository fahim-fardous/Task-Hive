package com.example.taskhive.components

import java.util.Date

data class TimerItem(
    val taskId: Int = 0,
    val time: Long = 0L,
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val isRunning: Boolean = false,
)
