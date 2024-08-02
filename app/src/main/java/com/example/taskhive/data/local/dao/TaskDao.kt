package com.example.taskhive.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(log: Log): Long

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE project = :project")
    suspend fun getTaskByProject(project: Project): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task

    @Query("SELECT COUNT(*) FROM tasks WHERE project = :project")
    suspend fun getTaskCountByProject(project: Project): Int

    @Query("SELECT * FROM logs WHERE taskId = :taskId ORDER BY startTime DESC")
    suspend fun getLogsByTaskId(taskId: Int): List<Log>

    @Query("SELECT * FROM tasks WHERE taskStatus = :taskStatus")
    suspend fun getInProgressTasks(taskStatus: TaskStatus = TaskStatus.IN_PROGRESS): List<Task>

    @Query("SELECT COUNT(*) FROM tasks WHERE taskStatus = :taskStatus")
    suspend fun getNumberOfInProgressTask(taskStatus: TaskStatus = TaskStatus.DONE): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE project = :project AND taskStatus = :taskStatus")
    suspend fun getInProgressTaskCountByProject(
        project: Project,
        taskStatus: TaskStatus = TaskStatus.IN_PROGRESS,
    ): Int

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT COUNT(*) FROM tasks WHERE project=:project AND taskStatus = :taskStatus")
    suspend fun getNumberOfCompletedTask(
        project: Project,
        taskStatus: TaskStatus = TaskStatus.DONE,
    ): Int
}
