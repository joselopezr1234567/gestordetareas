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

/**
 * ViewModel para la pantalla de crear/editar tarea
 * Implementa el patrón UDF/MVI para manejo unidireccional de estado
 */
class TaskFormViewModel(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(TaskFormUiState())
        private set
    
    /**
     * Maneja los eventos UI de la pantalla
     */
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
            is TaskFormUiEvent.ShowTimePicker -> showTimePicker()
            is TaskFormUiEvent.HideTimePicker -> hideTimePicker()
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
    
    /**
     * Carga una tarea existente para edición
     */
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
    
    /**
     * Actualiza el título y valida
     */
    private fun updateTitle(title: String) {
        uiState = uiState.copy(title = title, titleError = null)
        validateTitle()
    }
    
    /**
     * Actualiza la descripción y valida
     */
    private fun updateDescription(description: String) {
        uiState = uiState.copy(description = description, descriptionError = null)
        validateDescription()
    }
    
    /**
     * Actualiza la prioridad
     */
    private fun updatePriority(priority: TaskPriority) {
        uiState = uiState.copy(priority = priority, showPriorityDialog = false)
    }
    
    /**
     * Actualiza la fecha de vencimiento y valida
     */
    private fun updateDueDate(date: LocalDateTime?) {
        uiState = uiState.copy(dueDate = date, dueDateError = null, showDatePicker = false)
        validateDueDate()
        validateReminder() // Re-validar recordatorio si existe
    }
    
    /**
     * Actualiza la fecha y hora del recordatorio y valida
     */
    private fun updateReminderDateTime(dateTime: LocalDateTime?) {
        uiState = uiState.copy(reminderDateTime = dateTime, reminderError = null, showTimePicker = false)
        validateReminder()
    }
    
    /**
     * Valida el título
     */
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
    
    /**
     * Valida la descripción
     */
    private fun validateDescription() {
        val description = uiState.description.trim()
        uiState = uiState.copy(
            descriptionError = when {
                description.length > 500 -> "La descripción no puede exceder 500 caracteres"
                else -> null
            }
        )
    }
    
    /**
     * Valida la fecha de vencimiento
     */
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
    
    /**
     * Valida el recordatorio
     */
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
    
    /**
     * Valida todo el formulario
     */
    private fun validateForm() {
        validateTitle()
        validateDescription()
        validateDueDate()
        validateReminder()
    }
    
    /**
     * Muestra el selector de fecha
     */
    private fun showDatePicker() {
        uiState = uiState.copy(showDatePicker = true)
    }
    
    /**
     * Oculta el selector de fecha
     */
    private fun hideDatePicker() {
        uiState = uiState.copy(showDatePicker = false)
    }
    
    /**
     * Muestra el selector de hora
     */
    private fun showTimePicker() {
        uiState = uiState.copy(showTimePicker = true)
    }
    
    /**
     * Oculta el selector de hora
     */
    private fun hideTimePicker() {
        uiState = uiState.copy(showTimePicker = false)
    }
    
    /**
     * Muestra el diálogo de prioridad
     */
    private fun showPriorityDialog() {
        uiState = uiState.copy(showPriorityDialog = true)
    }
    
    /**
     * Oculta el diálogo de prioridad
     */
    private fun hidePriorityDialog() {
        uiState = uiState.copy(showPriorityDialog = false)
    }
    
    /**
     * Muestra el diálogo de descartar cambios
     */
    private fun showDiscardDialog() {
        uiState = uiState.copy(showDiscardDialog = true)
    }
    
    /**
     * Oculta el diálogo de descartar cambios
     */
    private fun hideDiscardDialog() {
        uiState = uiState.copy(showDiscardDialog = false)
    }
    
    /**
     * Guarda la tarea
     */
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
    
    /**
     * Descarta los cambios y navega hacia atrás
     */
    private fun discardChanges() {
        uiState = uiState.copy(
            showDiscardDialog = false,
            navigateBack = true
        )
    }
    
    /**
     * Navega hacia atrás
     */
    private fun navigateBack() {
        if (uiState.hasUnsavedChanges) {
            showDiscardDialog()
        } else {
            uiState = uiState.copy(navigateBack = true)
        }
    }
    
    /**
     * Limpia la fecha de vencimiento
     */
    private fun clearDueDate() {
        uiState = uiState.copy(dueDate = null, dueDateError = null)
        validateReminder() // Re-validar recordatorio
    }
    
    /**
     * Limpia el recordatorio
     */
    private fun clearReminder() {
        uiState = uiState.copy(reminderDateTime = null, reminderError = null)
    }
    
    /**
     * Descarta el error actual
     */
    private fun dismissError() {
        uiState = uiState.copy(error = null)
    }
    
    /**
     * Maneja el botón de retroceso
     */
    private fun onBackPressed() {
        navigateBack()
    }
    
    /**
     * Marca la navegación como manejada
     */
    private fun onNavigationHandled() {
        uiState = uiState.copy(navigateBack = false)
    }
    
    /**
     * Marca el guardado como manejado
     */
    private fun onTaskSavedHandled() {
        uiState = uiState.copy(taskSaved = false)
    }
}