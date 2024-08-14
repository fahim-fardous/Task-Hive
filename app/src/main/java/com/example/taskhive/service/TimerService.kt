package com.example.taskhive.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskhive.MainActivity
import com.example.taskhive.R
import com.example.taskhive.components.TimerItem
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val NOTIFICATION_ID = 1

    @Inject
    lateinit var taskRepository: TaskRepository

    companion object {
        private const val STOP_TIMER_ACTION = "STOP_TIMER_ACTION"
        private val _timerItem = MutableStateFlow<TimerItem?>(null)
        val timerItem = _timerItem.asStateFlow()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent?.action) {
            STOP_TIMER_ACTION -> {
                stopTimer()
            }

            else -> {
                val taskId = intent?.getIntExtra("taskId", 0) ?: 0
                val taskName = intent?.getStringExtra("taskName") ?: ""
                val currentTimer = _timerItem.value

                if (currentTimer?.isRunning == true) {
                    println("Another task is already running. Ignoring the new request.")
                } else {
                    startTimer(taskId, taskName)
                }
            }
        }
        return START_STICKY
    }

    private fun startTimer(
        taskId: Int,
        taskName: String,
    ) {
        stopTimer()

        val stopIntent =
            Intent(this, TimerService::class.java).apply {
                action = STOP_TIMER_ACTION
            }
        val stopPendingIntent =
            PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        _timerItem.value = TimerItem(taskId = taskId, isRunning = true, startTime = Date())

        updateNotification(taskName, formatTime(0L), stopPendingIntent)

        coroutineScope.launch {
            while (_timerItem.value?.isRunning == true) {
                delay(1000L)
                _timerItem.value = _timerItem.value?.copy(time = _timerItem.value!!.time + 1000L)

                updateNotification(
                    taskName,
                    formatTime(_timerItem.value?.time ?: 0L),
                    stopPendingIntent,
                )
            }
        }
    }

    private fun stopTimer() {
        _timerItem.value?.let {
            coroutineScope.launch {
                val task = taskRepository.getTaskById(it.taskId)
                taskRepository.saveLog(
                    Log(
                        duration = it.time,
                        startTime = it.startTime,
                        endTime = Date(),
                        taskId = it.taskId,
                    ),
                )
                taskRepository.saveTask(task.copy(totalTimeSpend = task.totalTimeSpend + it.time))
                taskRepository.getTaskByProject(task.plannedStartDate!!, task.project)
            }
        }
        _timerItem.value = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        stopTimer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val channel =
                NotificationChannel(
                    "TimerServiceChannel",
                    "Timer Service Channel",
                    NotificationManager.IMPORTANCE_LOW,
                )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(
        taskName: String,
        timerText: String,
        stopPendingIntent: PendingIntent,
    ) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification =
            NotificationCompat
                .Builder(this, "TimerServiceChannel")
                .setUsesChronometer(false)
                .setContentTitle("Task Hive")
                .setContentText("$taskName - $timerText")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_stat_name, "Stop Timer", stopPendingIntent)
                .setOngoing(true)
                .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun formatTime(millis: Long): String {
        val hours = millis / 3600000
        val minutes = (millis % 3600000) / 60000
        val seconds = (millis % 60000) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
