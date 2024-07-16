package com.example.taskhive.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import java.util.Date

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProject(project: Project): Long

    @Query("SELECT * FROM projects WHERE endDate >= :currentDate")
    suspend fun getAllProjects(currentDate:Date): List<Project>

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun getProjectCount(): Int

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): Project


}
