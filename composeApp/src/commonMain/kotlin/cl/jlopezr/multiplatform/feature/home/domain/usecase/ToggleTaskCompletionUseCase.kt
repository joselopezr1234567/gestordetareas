package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository

/**
 * Caso de uso para alternar el estado de completado de una tarea
 * Simplifica la operación de marcar/desmarcar tareas como completadas
 */
class ToggleTaskCompletionUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para alternar el estado de completado
     * 
     * @param taskId ID de la tarea a alternar
     * @return Result con la tarea actualizada o error
     */
    suspend operator fun invoke(taskId: String): Result<Task> {
        return repository.toggleTaskCompletion(taskId)
    }
}