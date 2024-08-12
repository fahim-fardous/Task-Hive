package com.example.taskhive.components


data class TimerItem(
    val taskId:Int = 0,
    val time:Long = 0L,
    val isRunning:Boolean = false,
)
