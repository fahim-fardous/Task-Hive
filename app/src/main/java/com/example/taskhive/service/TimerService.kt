package com.example.taskhive.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.taskhive.MainActivity
import com.example.taskhive.R
import com.example.taskhive.components.TimerItem
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.utils.localDateToDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
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
                return START_NOT_STICKY
            }

            else -> {
                val taskId = intent?.getIntExtra("taskId", 0) ?: 0
                val taskName = intent?.getStringExtra("taskName") ?: ""
                val projectId = intent?.getIntExtra("projectId", 0) ?: 0
                val currentTimer = _timerItem.value

                if (currentTimer?.isRunning == true) {
                    println("Timer already running")
                } else {
                    startTimer(taskId, taskName, projectId)
                }
            }
        }
        return START_STICKY
    }

    private fun startTimer(
        taskId: Int,
        taskName: String,
        projectId: Int? = null,
    ) {
        println("coming here to start")
        val stopIntent =
            Intent(this, TimerService::class.java).apply {
                action = STOP_TIMER_ACTION
            }
        val stopPendingIntent =
            PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        _timerItem.value =
            TimerItem(
                taskId = taskId,
                isRunning = true,
                startTime = localDateToDate(LocalDate.now()),
            )
        updateNotification(taskName, projectId, stopPendingIntent)
        startForeground(NOTIFICATION_ID, updateNotification(taskName, projectId, stopPendingIntent))

        coroutineScope.launch {
            while (_timerItem.value?.isRunning == true) {
                delay(1000L)
                _timerItem.value = _timerItem.value?.copy(time = _timerItem.value!!.time + 1000L)
            }
        }
    }

    private fun stopTimer() {
        _timerItem.value?.let {
            println(it.taskId)
            coroutineScope.launch {
                val task = taskRepository.getTaskById(it.taskId)
                taskRepository.saveLog(
                    Log(
                        duration = it.time,
                        startTime = it.startTime,
                        endTime = localDateToDate(LocalDate.now()),
                        taskId = it.taskId,
                        startDate = it.startTime,
                        endDate = localDateToDate(LocalDate.now()),
                    ),
                )
                taskRepository.saveTask(task.copy(totalTimeSpend = task.totalTimeSpend + it.time))
                println("Saved to database")
                _timerItem.value = null
                stopForeground(STOP_FOREGROUND_REMOVE)
                NotificationManagerCompat.from(this@TimerService).cancel(NOTIFICATION_ID)
                stopSelf()
            }
        }
        println("Timer stopped by clicking button")
        _timerItem.value = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        NotificationManagerCompat.from(this@TimerService).cancel(NOTIFICATION_ID)
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        println("Timer service destroyed")
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
        projectId: Int? = null,
        stopPendingIntent: PendingIntent,
    ): Notification {
        val notificationIntent =
            Intent(
                Intent.ACTION_VIEW,
                "taskhive://task/list/$projectId".toUri(),
                this,
                MainActivity::class.java,
            )
        val pendingIntent =
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(notificationIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

        val notification =
            NotificationCompat
                .Builder(this, "TimerServiceChannel")
                .setUsesChronometer(true)
                .setContentTitle("Task Hive")
                .setContentText(taskName)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_stat_name, "Stop Timer", stopPendingIntent)
                .setOngoing(true)
                .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
        return notification
    }
}
