package com.example.taskhive.base

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskHive : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
