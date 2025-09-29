package cl.jlopezr.multiplatform.feature.home.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.jlopezr.multiplatform.feature.home.data.model.TaskDto
import cl.jlopezr.multiplatform.feature.home.data.model.TaskListDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Data source local para gestionar tareas usando DataStore
 * Maneja la persistencia local de las tareas en formato JSON
 */
class TaskLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    
    companion object {
        private val TASKS_KEY = stringPreferencesKey("tasks")
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    /**
     * Obtiene todas las tareas como Flow
     */
    fun getAllTasks(): Flow<List<TaskDto>> {
        return dataStore.data.map { preferences ->
            val tasksJson = preferences[TASKS_KEY] ?: ""
            if (tasksJson.isEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<TaskListDto>(tasksJson).tasks
                } catch (e: Exception) {
                    println("Error deserializing tasks from DataStore: ${e.message}")
                    println("Corrupted JSON: $tasksJson")
                    // En caso de datos corruptos, devolver lista vacía para evitar crashes
                    emptyList()
                }
            }
        }
    }
    
    /**
     * Guarda una lista de tareas
     */
    suspend fun saveTasks(tasks: List<TaskDto>) {
        try {
            dataStore.edit { preferences ->
                val taskListDto = TaskListDto(tasks)
                val tasksJson = json.encodeToString(taskListDto)
                preferences[TASKS_KEY] = tasksJson
            }
        } catch (e: Exception) {
            println("Error saving tasks to DataStore: ${e.message}")
            throw e // Re-lanzar para que el repositorio pueda manejar el error
        }
    }
    
    /**
     * Obtiene una tarea específica por ID
     */
    suspend fun getTaskById(id: String): TaskDto? {
        return dataStore.data.map { preferences ->
            val tasksJson = preferences[TASKS_KEY] ?: ""
            if (tasksJson.isEmpty()) {
                null
            } else {
                try {
                    json.decodeFromString<TaskListDto>(tasksJson).tasks.find { it.id == id }
                } catch (e: Exception) {
                    null
                }
            }
        }.first()
    }
    
    /**
     * Agrega una nueva tarea
     */
    suspend fun addTask(task: TaskDto) {
        dataStore.edit { preferences ->
            val tasksJson = preferences[TASKS_KEY] ?: ""
            val currentTasks = if (tasksJson.isEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<TaskListDto>(tasksJson).tasks
                } catch (e: Exception) {
                    emptyList()
                }
            }
            
            val updatedTasks = currentTasks + task
            val taskListDto = TaskListDto(updatedTasks)
            preferences[TASKS_KEY] = json.encodeToString(taskListDto)
        }
    }
    
    /**
     * Actualiza una tarea existente
     */
    suspend fun updateTask(task: TaskDto) {
        try {
            dataStore.edit { preferences ->
                val tasksJson = preferences[TASKS_KEY] ?: ""
                val currentTasks = if (tasksJson.isEmpty()) {
                    emptyList()
                } else {
                    try {
                        json.decodeFromString<TaskListDto>(tasksJson).tasks
                    } catch (e: Exception) {
                        println("Error reading tasks for update: ${e.message}")
                        emptyList()
                    }
                }
                
                val updatedTasks = currentTasks.map { 
                    if (it.id == task.id) task else it 
                }
                
                // Verificar que la tarea fue encontrada y actualizada
                val taskFound = currentTasks.any { it.id == task.id }
                if (!taskFound) {
                    println("Warning: Task with id ${task.id} not found for update")
                }
                
                val taskListDto = TaskListDto(updatedTasks)
                preferences[TASKS_KEY] = json.encodeToString(taskListDto)
            }
        } catch (e: Exception) {
            println("Error updating task ${task.id}: ${e.message}")
            throw e // Re-lanzar para que el repositorio pueda manejar el error
        }
    }
    
    /**
     * Elimina una tarea por ID
     */
    suspend fun deleteTask(id: String) {
        dataStore.edit { preferences ->
            val tasksJson = preferences[TASKS_KEY] ?: ""
            val currentTasks = if (tasksJson.isEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<TaskListDto>(tasksJson).tasks
                } catch (e: Exception) {
                    emptyList()
                }
            }
            
            val updatedTasks = currentTasks.filter { it.id != id }
            val taskListDto = TaskListDto(updatedTasks)
            preferences[TASKS_KEY] = json.encodeToString(taskListDto)
        }
    }
    
    /**
     * Elimina todas las tareas completadas
     */
    suspend fun deleteCompletedTasks() {
        dataStore.edit { preferences ->
            val tasksJson = preferences[TASKS_KEY] ?: ""
            val currentTasks = if (tasksJson.isEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<TaskListDto>(tasksJson).tasks
                } catch (e: Exception) {
                    emptyList()
                }
            }
            
            val updatedTasks = currentTasks.filter { !it.isCompleted }
            val taskListDto = TaskListDto(updatedTasks)
            preferences[TASKS_KEY] = json.encodeToString(taskListDto)
        }
    }
    
    /**
     * Limpia todas las tareas (útil para testing o reset)
     */
    suspend fun clearAllTasks() {
        dataStore.edit { preferences ->
            preferences.remove(TASKS_KEY)
        }
    }
}