package cl.jlopezr.multiplatform.feature.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.feature.home.domain.usecase.TaskUseCases
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskDetailUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.state.TaskDetailUiState
import kotlinx.coroutines.launch


class TaskDetailViewModel(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(TaskDetailUiState())
        private set
    

    fun onEvent(event: TaskDetailUiEvent) {
        when (event) {
            is TaskDetailUiEvent.LoadTask -> loadTask(event.taskId)
            is TaskDetailUiEvent.ToggleTaskCompletion -> toggleTaskCompletion()
            is TaskDetailUiEvent.OnNavigationHandled -> onNavigationHandled()
            is TaskDetailUiEvent.DismissError -> dismissError()
        }
    }
    

    private fun loadTask(taskId: String) {
        if (taskId.isBlank()) {
            uiState = uiState.copy(
                error = "ID de tarea inválido",
                isLoading = false
            )
            return
        }
        
        uiState = uiState.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                val task = taskUseCases.getTaskById(taskId)
                if (task != null) {
                    uiState = uiState.copy(
                        task = task,
                        isLoading = false,
                        error = null
                    )
                } else {
                    uiState = uiState.copy(
                        task = null,
                        isLoading = false,
                        error = "Tarea no encontrada"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar la tarea"
                )
            }
        }
    }

    private fun toggleTaskCompletion() {
        val currentTask = uiState.task ?: return
        
        viewModelScope.launch {
            try {
                taskUseCases.toggleTaskCompletion(currentTask.id)
                    .onSuccess { updatedTask ->
                        uiState = uiState.copy(
                            task = updatedTask,
                            error = null
                        )
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            error = error.message ?: "Error al actualizar la tarea"
                        )
                    }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error al actualizar la tarea"
                )
            }
        }
    }
    

    private fun onNavigationHandled() {
        uiState = uiState.copy(navigateBack = false)
    }
    

    private fun dismissError() {
        uiState = uiState.copy(error = null)
    }
}