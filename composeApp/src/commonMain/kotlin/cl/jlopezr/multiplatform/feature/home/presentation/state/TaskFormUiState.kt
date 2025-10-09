package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


data class TaskFormUiState(

    val taskId: String? = null,
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null,
    

    val titleError: String? = null,
    val descriptionError: String? = null,
    val dueDateError: String? = null,
    val reminderError: String? = null,
    

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val showDatePicker: Boolean = false,
    val showPriorityDialog: Boolean = false,
    val showDiscardDialog: Boolean = false,
    

    val navigateBack: Boolean = false,
    val taskSaved: Boolean = false
) {
    

    val isEditMode: Boolean
        get() = taskId != null
    

    val isFormValid: Boolean
        get() = title.isNotBlank() && 
                titleError == null && 
                descriptionError == null && 
                dueDateError == null && 
                reminderError == null
    

    val hasUnsavedChanges: Boolean
        get() = title.isNotBlank() || 
                description.isNotBlank() || 
                dueDate != null || 
                reminderDateTime != null || 
                priority != TaskPriority.MEDIUM
    

    val canSave: Boolean
        get() = isFormValid && !isSaving && !isLoading

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