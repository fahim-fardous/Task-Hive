package com.example.taskhive.di

import com.example.taskhive.data.DayRepositoryImp
import com.example.taskhive.data.ProjectRepositoryImp
import com.example.taskhive.data.TaskRepositoryImp
import com.example.taskhive.domain.repository.DayRepository
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideTaskRepository(imp: TaskRepositoryImp): TaskRepository

    @Binds
    abstract fun provideProjectRepository(imp: ProjectRepositoryImp): ProjectRepository

    @Binds
    abstract fun provideDayRepository(imp: DayRepositoryImp): DayRepository
}