package com.example.taskhive.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskHive:Application() {
    override fun onCreate() {
        super.onCreate()

//        val serviceChannel = NotificationChannel(
//            "TimerServiceChannel",
//            "Time Service Channel",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        val manager = getSystemService(NotificationManager::class.java) as NotificationManager
//        manager.createNotificationChannel(serviceChannel)
    }
}