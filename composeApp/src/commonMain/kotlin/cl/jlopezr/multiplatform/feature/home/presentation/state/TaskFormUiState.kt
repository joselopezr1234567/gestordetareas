package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Estado UI para la pantalla de crear/editar tarea
 * Implementa el patrón UDF/MVI para manejo de estado unidireccional
 */
data class TaskFormUiState(
    // Datos de la tarea
    val taskId: String? = null,
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null,
    
    // Estados de validación
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dueDateError: String? = null,
    val reminderError: String? = null,
    
    // Estados de UI
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val showPriorityDialog: Boolean = false,
    val showDiscardDialog: Boolean = false,
    
    // Estados de navegación
    val navigateBack: Boolean = false,
    val taskSaved: Boolean = false
) {
    
    /**
     * Indica si es modo edición
     */
    val isEditMode: Boolean
        get() = taskId != null
    
    /**
     * Indica si el formulario es válido
     */
    val isFormValid: Boolean
        get() = title.isNotBlank() && 
                titleError == null && 
                descriptionError == null && 
                dueDateError == null && 
                reminderError == null
    
    /**
     * Indica si hay cambios sin guardar
     */
    val hasUnsavedChanges: Boolean
        get() = title.isNotBlank() || 
                description.isNotBlank() || 
                dueDate != null || 
                reminderDateTime != null || 
                priority != TaskPriority.MEDIUM
    
    /**
     * Indica si se puede guardar la tarea
     */
    val canSave: Boolean
        get() = isFormValid && !isSaving && !isLoading
    
    /**
     * Convierte el estado a una tarea del dominio
     */
    fun toTask(): Task? {
        if (!isFormValid) return null
        
        return Task(
            id = taskId ?: "",
            title = title.trim(),
            description = description.trim(),
            priority = priority,
            dueDate = dueDate,
            reminderDateTime = reminderDateTime,
            isCompleted = false,
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
            completedAt = null
        )
    }
    
    companion object {
        /**
         * Crea un estado desde una tarea existente para edición
         */
        fun fromTask(task: Task): TaskFormUiState {
            return TaskFormUiState(
                taskId = task.id,
                title = task.title,
                description = task.description ?: "",
                priority = task.priority,
                dueDate = task.dueDate,
                reminderDateTime = task.reminderDateTime
            )
        }
    }
}