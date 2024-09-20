package com.example.taskhiveapp.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskHive : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "File Upload"
        val descriptionText = "Notifications for file upload progress"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel("upload_channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

