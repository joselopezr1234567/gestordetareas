package cl.jlopezr.multiplatform.feature.home.presentation.event

import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder

/**
 * Eventos UI para la pantalla de lista de tareas
 * Define todas las acciones que el usuario puede realizar en la pantalla
 */
sealed class TaskListUiEvent {
    
    // Eventos de carga y actualización
    data object LoadTasks : TaskListUiEvent()
    data object RefreshTasks : TaskListUiEvent()
    
    // Eventos de búsqueda
    data class SearchQueryChanged(val query: String) : TaskListUiEvent()
    data object ToggleSearch : TaskListUiEvent()
    data object ClearSearch : TaskListUiEvent()
    
    // Eventos de filtrado
    data class FilterChanged(val filter: TaskFilter) : TaskListUiEvent()
    data object ShowFilterDialog : TaskListUiEvent()
    data object HideFilterDialog : TaskListUiEvent()
    
    // Eventos de ordenamiento
    data class SortOrderChanged(val sortOrder: TaskSortOrder) : TaskListUiEvent()
    data object ShowSortDialog : TaskListUiEvent()
    data object HideSortDialog : TaskListUiEvent()
    
    // Eventos de tareas
    data class ToggleTaskCompletion(val taskId: String) : TaskListUiEvent()
    data class DeleteTask(val taskId: String) : TaskListUiEvent()
    data class ShowDeleteConfirmation(val taskId: String) : TaskListUiEvent()
    data object HideDeleteConfirmation : TaskListUiEvent()
    data object ConfirmDeleteTask : TaskListUiEvent()
    data object DeleteCompletedTasks : TaskListUiEvent()
    
    // Eventos de navegación
    data object NavigateToCreateTask : TaskListUiEvent()
    data class NavigateToEditTask(val taskId: String) : TaskListUiEvent()
    data class NavigateToTaskDetail(val taskId: String) : TaskListUiEvent()
    data object NavigateToSettings : TaskListUiEvent()
    data object NavigateToStatistics : TaskListUiEvent()
    
    // Eventos de error
    data object DismissError : TaskListUiEvent()
    
    // Eventos de UI
    data object OnSwipeRefresh : TaskListUiEvent()
}