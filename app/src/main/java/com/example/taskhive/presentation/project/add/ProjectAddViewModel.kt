package com.example.taskhive.presentation.project.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectAddViewModel : ViewModel() {
    private val _showMessage = MutableStateFlow<String?>(null)
    val showMessage: StateFlow<String?> = _showMessage

    private fun isValid(
        name: String,
        description: String,
    ): Boolean {
        if (name.isBlank() || description.isBlank()) {
            _showMessage.value = "Fill all the fields"
            return false
        }
        return true
    }

    fun saveProject(
        project: Project,
        context: Context,
    ) = viewModelScope.launch {
        if (!isValid(project.name, project.description)) return@launch
        AppDatabase(context).projectDao().saveProject(project)
        _showMessage.value = "Project saved"
    }
}
