package cl.jlopezr.multiplatform.feature.home.presentation.event

import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder


sealed class TaskListUiEvent {
    

    data object LoadTasks : TaskListUiEvent()
    data object RefreshTasks : TaskListUiEvent()
    

    data class SearchQueryChanged(val query: String) : TaskListUiEvent()
    data object ToggleSearch : TaskListUiEvent()
    data object ClearSearch : TaskListUiEvent()
    

    data class FilterChanged(val filter: TaskFilter) : TaskListUiEvent()
    data object ShowFilterDialog : TaskListUiEvent()
    data object HideFilterDialog : TaskListUiEvent()
    

    data class SortOrderChanged(val sortOrder: TaskSortOrder) : TaskListUiEvent()
    data object ShowSortDialog : TaskListUiEvent()
    data object HideSortDialog : TaskListUiEvent()
    

    data class ToggleTaskCompletion(val taskId: String) : TaskListUiEvent()
    data class DeleteTask(val taskId: String) : TaskListUiEvent()
    data class ShowDeleteConfirmation(val taskId: String) : TaskListUiEvent()
    data object HideDeleteConfirmation : TaskListUiEvent()
    data object ConfirmDeleteTask : TaskListUiEvent()
    data object DeleteCompletedTasks : TaskListUiEvent()
    

    data object NavigateToCreateTask : TaskListUiEvent()
    data class NavigateToEditTask(val taskId: String) : TaskListUiEvent()
    data class NavigateToTaskDetail(val taskId: String) : TaskListUiEvent()
    data object NavigateToSettings : TaskListUiEvent()
    data object NavigateToStatistics : TaskListUiEvent()
    

    data object DismissError : TaskListUiEvent()
    

    data object OnSwipeRefresh : TaskListUiEvent()
}