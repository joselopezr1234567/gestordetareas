package cl.jlopezr.multiplatform.feature.home.domain.repository

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de dominio para gestionar tareas
 * Define las operaciones que se pueden realizar con las tareas
 */
interface TaskRepository {
    
    /**
     * Obtiene todas las tareas como un Flow para observar cambios en tiempo real
     */
    fun getAllTasks(): Flow<List<Task>>
    
    /**
     * Obtiene las tareas filtradas y ordenadas
     */
    fun getFilteredTasks(
        filter: TaskFilter = TaskFilter.ALL,
        sortOrder: TaskSortOrder = TaskSortOrder.DUE_DATE_ASC,
        searchQuery: String = ""
    ): Flow<List<Task>>
    
    /**
     * Obtiene una tarea específica por su ID
     */
    suspend fun getTaskById(id: String): Task?
    
    /**
     * Crea una nueva tarea
     */
    suspend fun createTask(task: Task): Result<Task>
    
    /**
     * Actualiza una tarea existente
     */
    suspend fun updateTask(task: Task): Result<Task>
    
    /**
     * Elimina una tarea por su ID
     */
    suspend fun deleteTask(id: String): Result<Unit>
    
    /**
     * Marca una tarea como completada o pendiente
     */
    suspend fun toggleTaskCompletion(id: String): Result<Task>
    
    /**
     * Elimina todas las tareas completadas
     */
    suspend fun deleteCompletedTasks(): Result<Unit>
    
    /**
     * Busca tareas por título o descripción
     */
    fun searchTasks(query: String): Flow<List<Task>>
    
    /**
     * Obtiene estadísticas de las tareas
     */
    suspend fun getTaskStatistics(): TaskStatistics
}

/**
 * Data class que contiene estadísticas de las tareas
 */
data class TaskStatistics(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val overdueTasks: Int,
    val todayTasks: Int,
    val highPriorityTasks: Int,
    val completionRate: Float
) {
    companion object {
        fun empty() = TaskStatistics(
            totalTasks = 0,
            completedTasks = 0,
            pendingTasks = 0,
            overdueTasks = 0,
            todayTasks = 0,
            highPriorityTasks = 0,
            completionRate = 0f
        )
    }
}