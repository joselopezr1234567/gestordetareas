package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder

/**
 * Estado UI para la pantalla de lista de tareas
 * Implementa el patrón UDF/MVI para manejo de estado unidireccional
 */
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
    
    /**
     * Indica si hay tareas para mostrar
     */
    val hasTasks: Boolean
        get() = tasks.isNotEmpty()
    
    /**
     * Indica si se está mostrando el estado vacío
     */
    val showEmptyState: Boolean
        get() = !isLoading && !hasTasks && error == null
    
    /**
     * Indica si se debe mostrar el indicador de carga
     */
    val showLoadingIndicator: Boolean
        get() = isLoading && !isRefreshing
    
    /**
     * Obtiene el número de tareas filtradas
     */
    val filteredTasksCount: Int
        get() = tasks.size
    
    /**
     * Obtiene el número de tareas completadas
     */
    val completedTasksCount: Int
        get() = tasks.count { it.isCompleted }
    
    /**
     * Obtiene el número de tareas pendientes
     */
    val pendingTasksCount: Int
        get() = tasks.count { !it.isCompleted }
    
    /**
     * Indica si hay tareas completadas para eliminar
     */
    val hasCompletedTasks: Boolean
        get() = tasks.any { it.isCompleted }
}