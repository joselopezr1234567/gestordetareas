package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository

/**
 * Caso de uso para obtener una tarea específica por su ID
 */
class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    
    /**
     * Obtiene una tarea por su ID
     * 
     * @param taskId ID de la tarea a buscar
     * @return La tarea encontrada o null si no existe
     * @throws IllegalArgumentException si el ID está vacío
     */
    suspend operator fun invoke(taskId: String): Task? {
        require(taskId.isNotBlank()) { "El ID de la tarea no puede estar vacío" }
        
        return taskRepository.getTaskById(taskId)
    }
}