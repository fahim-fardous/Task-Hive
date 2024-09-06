package com.example.taskhive.presentation.project.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectAddViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
    ) : ViewModel() {
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

        fun saveProject(project: Project) =
            viewModelScope.launch {
                if (!isValid(project.name, project.description)) return@launch
                projectRepository.saveProject(project)
                _showMessage.value = "Project saved"
            }

        fun updateMessage() =
            viewModelScope.launch {
                _showMessage.value = null
            }
    }
