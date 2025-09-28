package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory

/**
 * Caso de uso para alternar el estado de completado de una tarea
 * Simplifica la operación de marcar/desmarcar tareas como completadas
 */
class ToggleTaskCompletionUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    
    /**
     * Ejecuta el caso de uso para alternar el estado de completado
     * 
     * @param taskId ID de la tarea a alternar
     * @return Result con la tarea actualizada o error
     */
    suspend operator fun invoke(taskId: String): Result<Task> {
        val result = repository.toggleTaskCompletion(taskId)
        
        // Gestionar notificaciones según el nuevo estado
        if (result.isSuccess) {
            val updatedTask = result.getOrNull()!!
            
            if (updatedTask.isCompleted) {
                // Si la tarea se completó, cancelar notificación
                notificationManager.cancelNotification(taskId)
            } else if (updatedTask.reminderDateTime != null) {
                // Si se descompletó y tiene recordatorio, reprogramar notificación
                notificationManager.scheduleNotification(
                    id = updatedTask.id,
                    title = "Recordatorio de tarea",
                    message = updatedTask.title,
                    dateTime = updatedTask.reminderDateTime
                )
            }
        }
        
        return result
    }
}