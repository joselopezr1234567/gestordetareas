package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory

/**
 * Caso de uso para eliminar una tarea
 * Incluye validaciones antes de la eliminación
 */
class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    
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
        
        val result = repository.deleteTask(taskId)
        
        // Cancelar notificación si la eliminación fue exitosa
        if (result.isSuccess) {
            notificationManager.cancelNotification(taskId)
        }
        
        return result
    }
}