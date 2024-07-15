package com.example.taskhive.di

import android.content.Context
import com.example.taskhive.data.ProjectRepositoryImp
import com.example.taskhive.data.TaskRepositoryImp
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import dagger.Binds
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
}
