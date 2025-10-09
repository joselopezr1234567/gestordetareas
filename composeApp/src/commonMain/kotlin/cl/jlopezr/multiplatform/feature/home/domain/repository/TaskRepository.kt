package cl.jlopezr.multiplatform.feature.home.domain.repository

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder
import kotlinx.coroutines.flow.Flow


interface TaskRepository {
    

    fun getAllTasks(): Flow<List<Task>>
    

    fun getFilteredTasks(
        filter: TaskFilter = TaskFilter.ALL,
        sortOrder: TaskSortOrder = TaskSortOrder.DUE_DATE_ASC,
        searchQuery: String = ""
    ): Flow<List<Task>>
    

    suspend fun getTaskById(id: String): Task?
    

    suspend fun createTask(task: Task): Result<Task>
    

    suspend fun updateTask(task: Task): Result<Task>
    

    suspend fun deleteTask(id: String): Result<Unit>
    

    suspend fun toggleTaskCompletion(id: String): Result<Task>
    

    suspend fun deleteCompletedTasks(): Result<Unit>
    

    fun searchTasks(query: String): Flow<List<Task>>
    

    suspend fun getTaskStatistics(): TaskStatistics
}


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