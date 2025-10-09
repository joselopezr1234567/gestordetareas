package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory


class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    

    suspend operator fun invoke(taskId: String): Result<Unit> {
        

        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("El ID de la tarea no puede estar vacío"))
        }
        

        val existingTask = repository.getTaskById(taskId)
            ?: return Result.failure(IllegalArgumentException("La tarea no existe"))
        
        val result = repository.deleteTask(taskId)
        

        if (result.isSuccess) {
            notificationManager.cancelNotification(taskId)
        }
        
        return result
    }
}