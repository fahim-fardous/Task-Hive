package com.example.taskhive.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskhive.domain.model.Entry
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.ProjectProgress
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.model.TaskWithEntries
import java.util.Date

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveEntry(entry: Entry): Long

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithEntries(taskId: Int): TaskWithEntries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLog(log: Log): Long

    @Query("SELECT * FROM tasks")
    suspend fun getAllTask(): List<Task>

    @Query(
        """
    SELECT * FROM tasks AS t 
    INNER JOIN entries AS e ON t.id = e.taskId 
    WHERE e.date = :date
""",
    )
    suspend fun getTaskWithEntries(date: Date): List<TaskWithEntries>

    @Query("SELECT COUNT(*) FROM entries WHERE date=:date AND taskId=:taskId")
    suspend fun getEntryByDate(
        date: Date,
        taskId: Int,
    ): Int

    @Query("SELECT * FROM entries")
    suspend fun getAllEntry(): List<Entry>

    @Query("SELECT * FROM tasks WHERE plannedStartDate = :date AND project = :project")
    suspend fun getTaskByProject(
        date: Date,
        project: Project,
    ): List<Task>

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
    suspend fun getNumberOfCompletedTaskByProject(
        project: Project,
        taskStatus: TaskStatus = TaskStatus.DONE,
    ): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE plannedStartDate = :startDate AND taskStatus = :taskStatus")
    suspend fun getNumberOfCompletedTask(
        startDate: Date,
        taskStatus: TaskStatus = TaskStatus.DONE,
    ): Int

    @Query("SELECT * FROM tasks WHERE plannedStartDate=:date AND (project=:project OR :project IS NULL)")
    suspend fun getTodaysTasks(
        date: Date,
        project: Project?,
    ): List<Task>

    @Query("SELECT * FROM tasks WHERE plannedStartDate=:date")
    suspend fun getAllTasks(date: Date): List<Task>

    @Query(
        """
        SELECT 
            DATE(plannedStartDate / 1000, 'unixepoch', 'localtime') as date, 
            SUM(totalTimeSpend) as totalTimeSpent
        FROM tasks
        WHERE plannedStartDate IS NOT NULL 
        AND plannedStartDate BETWEEN :startDate AND :endDate
        GROUP BY DATE(plannedStartDate / 1000, 'unixepoch', 'localtime')
        ORDER BY date ASC
    """,
    )
    suspend fun getProgressForWeek(
        startDate: Date,
        endDate: Date,
    ): List<ProjectProgress>

    @Query("SELECT * FROM tasks WHERE actualStartTime BETWEEN :startDate AND :endDate")
    suspend fun getWeeklyTask(
        startDate: Date,
        endDate: Date,
    ): List<Task>

    @Query("SELECT * FROM tasks WHERE plannedStartDate BETWEEN :startDate AND :endDate")
    suspend fun getTaskByRange(
        startDate: Date,
        endDate: Date,
    ): List<Task>

    @Query("SELECT * FROM tasks WHERE project = :project AND plannedStartDate BETWEEN :startDate AND :endDate")
    suspend fun getTaskByRangeAndProject(
        startDate: Date,
        endDate: Date,
        project: Project,
    ): List<Task>
}
