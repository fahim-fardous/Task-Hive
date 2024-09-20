package com.example.taskhiveapp.di

import com.example.taskhiveapp.data.DayRepositoryImp
import com.example.taskhiveapp.data.ProjectRepositoryImp
import com.example.taskhiveapp.data.TaskRepositoryImp
import com.example.taskhiveapp.domain.repository.DayRepository
import com.example.taskhiveapp.domain.repository.ProjectRepository
import com.example.taskhiveapp.domain.repository.TaskRepository
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