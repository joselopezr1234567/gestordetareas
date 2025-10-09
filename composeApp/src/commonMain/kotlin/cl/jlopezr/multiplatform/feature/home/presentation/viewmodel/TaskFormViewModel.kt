package cl.jlopezr.multiplatform.feature.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import cl.jlopezr.multiplatform.feature.home.domain.usecase.TaskUseCases
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskFormUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.state.TaskFormUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime


class TaskFormViewModel(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(TaskFormUiState())
        private set
    

    fun onEvent(event: TaskFormUiEvent) {
        when (event) {
            is TaskFormUiEvent.LoadTask -> loadTask(event.taskId)
            is TaskFormUiEvent.TitleChanged -> updateTitle(event.title)
            is TaskFormUiEvent.DescriptionChanged -> updateDescription(event.description)
            is TaskFormUiEvent.PriorityChanged -> updatePriority(event.priority)
            is TaskFormUiEvent.DueDateChanged -> updateDueDate(event.date)
            is TaskFormUiEvent.ReminderDateTimeChanged -> updateReminderDateTime(event.dateTime)
            is TaskFormUiEvent.ValidateTitle -> validateTitle()
            is TaskFormUiEvent.ValidateDescription -> validateDescription()
            is TaskFormUiEvent.ValidateDueDate -> validateDueDate()
            is TaskFormUiEvent.ValidateReminder -> validateReminder()
            is TaskFormUiEvent.ValidateForm -> validateForm()
            is TaskFormUiEvent.ShowDatePicker -> showDatePicker()
            is TaskFormUiEvent.HideDatePicker -> hideDatePicker()
            is TaskFormUiEvent.ShowPriorityDialog -> showPriorityDialog()
            is TaskFormUiEvent.HidePriorityDialog -> hidePriorityDialog()
            is TaskFormUiEvent.ShowDiscardDialog -> showDiscardDialog()
            is TaskFormUiEvent.HideDiscardDialog -> hideDiscardDialog()
            is TaskFormUiEvent.SaveTask -> saveTask()
            is TaskFormUiEvent.DiscardChanges -> discardChanges()
            is TaskFormUiEvent.NavigateBack -> navigateBack()
            is TaskFormUiEvent.ClearDueDate -> clearDueDate()
            is TaskFormUiEvent.ClearReminder -> clearReminder()
            is TaskFormUiEvent.DismissError -> dismissError()
            is TaskFormUiEvent.OnBackPressed -> onBackPressed()
            is TaskFormUiEvent.OnNavigationHandled -> onNavigationHandled()
            is TaskFormUiEvent.OnTaskSavedHandled -> onTaskSavedHandled()
        }
    }
    

    private fun loadTask(taskId: String) {
        uiState = uiState.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                val task = taskUseCases.getTaskById(taskId)
                if (task != null) {
                    uiState = TaskFormUiState.fromTask(task).copy(isLoading = false)
                } else {
                    uiState = uiState.copy(
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
    

    private fun updateTitle(title: String) {
        uiState = uiState.copy(title = title, titleError = null)
        validateTitle()
    }
    

    private fun updateDescription(description: String) {
        uiState = uiState.copy(description = description, descriptionError = null)
        validateDescription()
    }
    

    private fun updatePriority(priority: TaskPriority) {
        uiState = uiState.copy(priority = priority, showPriorityDialog = false)
    }
    

    private fun updateDueDate(date: LocalDateTime?) {
        uiState = uiState.copy(dueDate = date, dueDateError = null, showDatePicker = false)
        validateDueDate()
        validateReminder() // Re-validar recordatorio si existe
    }
    

    private fun updateReminderDateTime(dateTime: LocalDateTime?) {
        uiState = uiState.copy(reminderDateTime = dateTime, reminderError = null)
        validateReminder()
    }
    

    private fun validateTitle() {
        val title = uiState.title.trim()
        uiState = uiState.copy(
            titleError = when {
                title.isBlank() -> "El título es obligatorio"
                title.length < 3 -> "El título debe tener al menos 3 caracteres"
                title.length > 100 -> "El título no puede exceder 100 caracteres"
                else -> null
            }
        )
    }
    

    private fun validateDescription() {
        val description = uiState.description.trim()
        uiState = uiState.copy(
            descriptionError = when {
                description.length > 500 -> "La descripción no puede exceder 500 caracteres"
                else -> null
            }
        )
    }
    

    private fun validateDueDate() {
        val dueDate = uiState.dueDate
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        
        uiState = uiState.copy(
            dueDateError = when {
                dueDate != null && dueDate < now -> "La fecha de vencimiento no puede ser anterior a ahora"
                else -> null
            }
        )
    }
    

    private fun validateReminder() {
        val reminder = uiState.reminderDateTime
        val dueDate = uiState.dueDate
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        
        uiState = uiState.copy(
            reminderError = when {
                reminder != null && reminder < now -> 
                    "El recordatorio no puede ser anterior a ahora"
                reminder != null && dueDate != null && reminder > dueDate -> 
                    "El recordatorio no puede ser posterior a la fecha de vencimiento"
                else -> null
            }
        )
    }
    

    private fun validateForm() {
        validateTitle()
        validateDescription()
        validateDueDate()
        validateReminder()
    }
    

    private fun showDatePicker() {
        uiState = uiState.copy(showDatePicker = true)
    }
    

    private fun hideDatePicker() {
        uiState = uiState.copy(showDatePicker = false)
    }
    

    private fun showPriorityDialog() {
        uiState = uiState.copy(showPriorityDialog = true)
    }
    

    private fun hidePriorityDialog() {
        uiState = uiState.copy(showPriorityDialog = false)
    }
    

    private fun showDiscardDialog() {
        uiState = uiState.copy(showDiscardDialog = true)
    }
    

    private fun hideDiscardDialog() {
        uiState = uiState.copy(showDiscardDialog = false)
    }
    

    private fun saveTask() {
        validateForm()
        
        if (!uiState.isFormValid) return
        
        val task = uiState.toTask()!! // Safe because we already validated the form
        
        uiState = uiState.copy(isSaving = true, error = null)
        
        viewModelScope.launch {
            try {
                val result = if (!uiState.taskId.isNullOrEmpty()) {
                    taskUseCases.updateTask(task)
                } else {
                    taskUseCases.createTask(
                        title = uiState.title,
                        description = uiState.description.takeIf { it.isNotBlank() },
                        priority = uiState.priority,
                        dueDate = uiState.dueDate,
                        reminderDateTime = uiState.reminderDateTime
                    )
                }
                
                result
                    .onSuccess {
                        uiState = uiState.copy(
                            isSaving = false,
                            taskSaved = true
                        )
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            isSaving = false,
                            error = error.message ?: "Error al guardar la tarea"
                        )
                    }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isSaving = false,
                    error = e.message ?: "Error inesperado"
                )
            }
        }
    }
    

    private fun discardChanges() {
        uiState = uiState.copy(
            showDiscardDialog = false,
            navigateBack = true
        )
    }
    

    private fun navigateBack() {
        if (uiState.hasUnsavedChanges) {
            showDiscardDialog()
        } else {
            uiState = uiState.copy(navigateBack = true)
        }
    }
    

    private fun clearDueDate() {
        uiState = uiState.copy(dueDate = null, dueDateError = null)
        validateReminder()
    }
    

    private fun clearReminder() {
        uiState = uiState.copy(reminderDateTime = null, reminderError = null)
    }
    

    private fun dismissError() {
        uiState = uiState.copy(error = null)
    }
    

    private fun onBackPressed() {
        navigateBack()
    }
    

    private fun onNavigationHandled() {
        uiState = uiState.copy(navigateBack = false)
    }
    

    private fun onTaskSavedHandled() {
        uiState = uiState.copy(taskSaved = false)
    }
}