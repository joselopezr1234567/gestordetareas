package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository

/**
 * Caso de uso para eliminar una tarea
 * Incluye validaciones antes de la eliminación
 */
class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para eliminar una tarea
     * 
     * @param taskId ID de la tarea a eliminar
     * @return Result indicando éxito o error
     */
    suspend operator fun invoke(taskId: String): Result<Unit> {
        
        // Validar que el ID no esté vacío
        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("El ID de la tarea no puede estar vacío"))
        }
        
        // Verificar que la tarea existe
        val existingTask = repository.getTaskById(taskId)
            ?: return Result.failure(IllegalArgumentException("La tarea no existe"))
        
        return repository.deleteTask(taskId)
    }
}