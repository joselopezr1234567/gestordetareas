package cl.jlopezr.multiplatform.feature.home.presentation.event

import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime


sealed class TaskFormUiEvent {
    

    data class LoadTask(val taskId: String) : TaskFormUiEvent()
    

    data class TitleChanged(val title: String) : TaskFormUiEvent()
    data class DescriptionChanged(val description: String) : TaskFormUiEvent()
    data class PriorityChanged(val priority: TaskPriority) : TaskFormUiEvent()
    data class DueDateChanged(val date: LocalDateTime?) : TaskFormUiEvent()
    data class ReminderDateTimeChanged(val dateTime: LocalDateTime?) : TaskFormUiEvent()
    

    data object ValidateTitle : TaskFormUiEvent()
    data object ValidateDescription : TaskFormUiEvent()
    data object ValidateDueDate : TaskFormUiEvent()
    data object ValidateReminder : TaskFormUiEvent()
    data object ValidateForm : TaskFormUiEvent()
    

    data object ShowDatePicker : TaskFormUiEvent()
    data object HideDatePicker : TaskFormUiEvent()
    data object ShowPriorityDialog : TaskFormUiEvent()
    data object HidePriorityDialog : TaskFormUiEvent()
    data object ShowDiscardDialog : TaskFormUiEvent()
    data object HideDiscardDialog : TaskFormUiEvent()
    

    data object SaveTask : TaskFormUiEvent()
    data object DiscardChanges : TaskFormUiEvent()
    data object NavigateBack : TaskFormUiEvent()
    data object ClearDueDate : TaskFormUiEvent()
    data object ClearReminder : TaskFormUiEvent()
    
    // Eventos de error
    data object DismissError : TaskFormUiEvent()
    
    // Eventos de navegación
    data object OnBackPressed : TaskFormUiEvent()
    data object OnNavigationHandled : TaskFormUiEvent()
    data object OnTaskSavedHandled : TaskFormUiEvent()
}