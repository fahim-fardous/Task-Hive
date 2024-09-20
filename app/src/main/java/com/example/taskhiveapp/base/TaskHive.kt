package com.example.taskhiveapp.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskHive : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
