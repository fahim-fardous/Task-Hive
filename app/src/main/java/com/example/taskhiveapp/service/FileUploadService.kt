package com.example.taskhiveapp.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.taskhiveapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class FileUploadService:Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val NOTIFICATION_ID = 2

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotification():Notification {
        notificationBuilder = NotificationCompat.Builder(this, "FileUploadServiceChannel")
            .setContentTitle("Task Hive")
            .setContentText("Uploading file to cloud")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setProgress(100, 0, false)
        return notificationBuilder.build()
    }

    private fun updateNotification(progress: Int){
        notificationBuilder.setProgress(100, progress, false)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())


    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}