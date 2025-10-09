package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder


data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val currentFilter: TaskFilter = TaskFilter.PENDING,
    val currentSortOrder: TaskSortOrder = TaskSortOrder.CREATED_DATE_DESC,
    val isSearchActive: Boolean = false,
    val showFilterDialog: Boolean = false,
    val showSortDialog: Boolean = false,
    val selectedTaskId: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val isRefreshing: Boolean = false
) {
    

    val hasTasks: Boolean
        get() = tasks.isNotEmpty()
    

    val showEmptyState: Boolean
        get() = !isLoading && !hasTasks && error == null
    

    val showLoadingIndicator: Boolean
        get() = isLoading && !isRefreshing
    

    val filteredTasksCount: Int
        get() = tasks.size
    

    val completedTasksCount: Int
        get() = tasks.count { it.isCompleted }
    

    val pendingTasksCount: Int
        get() = tasks.count { !it.isCompleted }
    

    val hasCompletedTasks: Boolean
        get() = tasks.any { it.isCompleted }
}