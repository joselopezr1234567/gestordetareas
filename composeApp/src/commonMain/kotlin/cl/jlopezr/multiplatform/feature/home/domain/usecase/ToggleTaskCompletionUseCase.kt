package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory


class ToggleTaskCompletionUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    

    suspend operator fun invoke(taskId: String): Result<Task> {
        val result = repository.toggleTaskCompletion(taskId)
        

        if (result.isSuccess) {
            val updatedTask = result.getOrNull()!!
            
            if (updatedTask.isCompleted) {

                notificationManager.cancelNotification(taskId)
            } else if (updatedTask.reminderDateTime != null) {

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