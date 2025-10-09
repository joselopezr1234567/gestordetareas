package cl.jlopezr.multiplatform.feature.home.data.repository

import cl.jlopezr.multiplatform.feature.home.data.datasource.TaskLocalDataSource
import cl.jlopezr.multiplatform.feature.home.data.model.toDto
import cl.jlopezr.multiplatform.feature.home.data.model.toDomain
import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime


class TaskRepositoryImpl(
    private val localDataSource: TaskLocalDataSource
) : TaskRepository {
    
    override fun getAllTasks(): Flow<List<Task>> {
        return localDataSource.getAllTasks().map { taskDtos ->
            taskDtos.map { it.toDomain() }
        }
    }
    
    override fun getFilteredTasks(
        filter: TaskFilter,
        sortOrder: TaskSortOrder,
        searchQuery: String
    ): Flow<List<Task>> {
        return getAllTasks().map { tasks ->
            var filteredTasks = tasks
            

            filteredTasks = when (filter) {
                TaskFilter.ALL -> filteredTasks
                TaskFilter.PENDING -> filteredTasks.filter { !it.isCompleted }
                TaskFilter.COMPLETED -> filteredTasks.filter { it.isCompleted }
            }
            

            if (searchQuery.isNotBlank()) {
                filteredTasks = filteredTasks.filter { task ->
                    task.title.contains(searchQuery, ignoreCase = true) ||
                    task.description?.contains(searchQuery, ignoreCase = true) == true
                }
            }
            

            when (sortOrder) {
                TaskSortOrder.CREATED_DATE_DESC -> filteredTasks.sortedByDescending { it.createdAt }
                TaskSortOrder.CREATED_DATE_ASC -> filteredTasks.sortedBy { it.createdAt }
                TaskSortOrder.DUE_DATE_ASC -> filteredTasks.sortedWith(
                    compareBy<Task> { it.dueDate == null }.thenBy { it.dueDate }
                )
                TaskSortOrder.DUE_DATE_DESC -> filteredTasks.sortedWith(
                    compareBy<Task> { it.dueDate == null }.thenByDescending { it.dueDate }
                )
                TaskSortOrder.PRIORITY_DESC -> filteredTasks.sortedByDescending { it.priority.ordinal }
                TaskSortOrder.PRIORITY_ASC -> filteredTasks.sortedBy { it.priority.ordinal }
                TaskSortOrder.TITLE_ASC -> filteredTasks.sortedBy { it.title.lowercase() }
                TaskSortOrder.TITLE_DESC -> filteredTasks.sortedByDescending { it.title.lowercase() }
                TaskSortOrder.COMPLETION_STATUS -> filteredTasks.sortedWith(
                    compareBy<Task> { it.isCompleted }.thenByDescending { it.createdAt }
                )
            }
        }
    }
    
    override suspend fun getTaskById(id: String): Task? {
        return localDataSource.getTaskById(id)?.toDomain()
    }
    
    override suspend fun createTask(task: Task): Result<Task> {
        return try {
            localDataSource.addTask(task.toDto())
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateTask(task: Task): Result<Task> {
        return try {

            val existingTask = localDataSource.getTaskById(task.id)
            if (existingTask == null) {
                Result.failure(IllegalArgumentException("Task with id ${task.id} not found"))
            } else {
                localDataSource.updateTask(task.toDto())
                Result.success(task)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteTask(id: String): Result<Unit> {
        return try {

            val existingTask = localDataSource.getTaskById(id)
            if (existingTask == null) {
                Result.failure(IllegalArgumentException("Task with id $id not found"))
            } else {
                localDataSource.deleteTask(id)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun toggleTaskCompletion(id: String): Result<Task> {
        return try {
            val existingTask = localDataSource.getTaskById(id)?.toDomain()
            if (existingTask == null) {
                Result.failure(IllegalArgumentException("Task with id $id not found"))
            } else {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val updatedTask = existingTask.copy(
                    isCompleted = !existingTask.isCompleted,
                    completedAt = if (!existingTask.isCompleted) now else null,
                    updatedAt = now
                )
                localDataSource.updateTask(updatedTask.toDto())
                Result.success(updatedTask)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteCompletedTasks(): Result<Unit> {
        return try {
            localDataSource.deleteCompletedTasks()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchTasks(query: String): Flow<List<Task>> {
        return getAllTasks().map { tasks ->
            if (query.isBlank()) {
                tasks
            } else {
                tasks.filter { task ->
                    task.title.contains(query, ignoreCase = true) ||
                    task.description?.contains(query, ignoreCase = true) == true
                }
            }
        }
    }
    
    override suspend fun getTaskStatistics(): TaskStatistics {
        val allTasks = getAllTasks().first()
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        
        val totalTasks = allTasks.size
        val completedTasks = allTasks.count { it.isCompleted }
        val pendingTasks = allTasks.count { !it.isCompleted }
        val overdueTasks = allTasks.count { !it.isCompleted && it.isOverdue() }
        val todayTasks = allTasks.count { !it.isCompleted && it.dueDate == today }
        val highPriorityTasks = allTasks.count { 
            !it.isCompleted && it.priority == TaskPriority.HIGH 
        }
        
        val completionRate = if (totalTasks > 0) {
            (completedTasks.toFloat() / totalTasks.toFloat()) * 100
        } else {
            0f
        }
        
        return TaskStatistics(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            pendingTasks = pendingTasks,
            overdueTasks = overdueTasks,
            todayTasks = todayTasks,
            highPriorityTasks = highPriorityTasks,
            completionRate = completionRate
        )
    }
}