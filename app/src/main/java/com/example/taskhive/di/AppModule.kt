package com.example.taskhive.di

import android.content.Context
import android.content.SharedPreferences
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.service.TimerService
import com.example.taskhive.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = AppDatabase(context)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferenceHelper(preferences: SharedPreferences): PreferenceHelper = PreferenceHelper(preferences)

}
