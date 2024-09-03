package com.example.taskhive.presentation.analytics

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.DayRepository
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.ProjectUiModel
import com.example.taskhive.utils.formatDateToDDMMYYYY
import com.example.taskhive.utils.formatTime
import com.example.taskhive.utils.getReadableTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel
    @Inject
    constructor(
        private val dayRepository: DayRepository,
        private val taskRepository: TaskRepository,
        private val projectRepository: ProjectRepository,
    ) : ViewModel() {
        private val _showMessage = MutableStateFlow<String?>(null)
        val showMessage = _showMessage.asStateFlow()

        private val _weeklyTask = MutableStateFlow<List<Task>>(emptyList())
        val weeklyTask = _weeklyTask.asStateFlow()

        private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
        val projects = _projects.asStateFlow()

        private val today = LocalDate.now()
        private val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        private val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        fun getAllProjects() =
            viewModelScope.launch {
                val projects = projectRepository.getAllProjects()
                if (projects.isNotEmpty()) {
                    _projects.value =
                        projects.map {
                            it.toUiModel()
                        }
                }
            }

        fun getWeeklyTask(
            startDate: Date = Date(),
            endDate: Date = Date(),
        ) = viewModelScope.launch {
            val weeklyTasks = taskRepository.getWeeklyTask(startDate, endDate)
            if (weeklyTasks.isNotEmpty()) {
                _weeklyTask.value = weeklyTasks
            } else {
                _showMessage.value = "No tasks found for the selected date range."
            }
        }

        fun downloadReport(
            context: Context,
            selectedProjects: List<ProjectUiModel>,
        ) = viewModelScope.launch {
            val startDate = localDateToDate(startOfWeek)
            val endDate = localDateToDate(endOfWeek)
            val weeklyTasks = taskRepository.getWeeklyTask(startDate, endDate)
            val weeklyTasksByProject =
                weeklyTasks.filter { task -> selectedProjects.any { it.id == task.project.id } }
            val csvContent = generateCsvContent(weeklyTasksByProject)
            val csvFile = saveCsvFile(context, csvContent)

            if (csvFile != null) {
                shareCsvFile(context, csvFile)
            } else {
                // Handle the error
                Toast.makeText(context, "Error saving CSV file", Toast.LENGTH_SHORT).show()
            }
        }

        private fun generateCsvContent(tasks: List<Task>): String {
            val csvBuilder = StringBuilder()
            csvBuilder.append("Project, Task, Tag, Start Date, Start Time, End Time, Duration\n")
            for (task in tasks) {
                csvBuilder.append(
                    "${task.project.name}, ${task.title}, Tag, ${
                        formatDateToDDMMYYYY(
                            task.plannedStartDate!!,
                        )
                    }, ${task.plannedStartTime.getReadableTime()}, ${task.plannedEndTime.getReadableTime()}, ${
                        formatTime(
                            task.totalTimeSpend,
                        )
                    }\n",
                )
            }
            return csvBuilder.toString()
        }

        private fun saveCsvFile(
            context: Context,
            csvContent: String,
        ): File? {
            val fileName = "task_report.csv"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            try {
                val fileWriter = FileWriter(file)
                fileWriter.append(csvContent)
                fileWriter.flush()
                fileWriter.close()
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        private fun shareCsvFile(
            context: Context,
            file: File,
        ) {
            val fileUri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val shareIntent =
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    setDataAndType(fileUri, "text/csv")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
            context.startActivity(Intent.createChooser(shareIntent, "Share CSV File"))
        }

        private fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())

        fun resetMessage() {
            _showMessage.value = null
        }
    }
