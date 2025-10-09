package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class UpdateTaskUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    

    suspend operator fun invoke(task: Task): Result<Task> {
        

        if (task.title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }
        
        if (task.title.length > 100) {
            return Result.failure(IllegalArgumentException("El título no puede exceder 100 caracteres"))
        }
        
        if (task.description != null && task.description.length > 500) {
            return Result.failure(IllegalArgumentException("La descripción no puede exceder 500 caracteres"))
        }
        
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        

        if (task.dueDate != null && task.dueDate < now && !task.isCompleted) {
            return Result.failure(IllegalArgumentException("La fecha límite no puede ser anterior a la fecha actual para tareas pendientes"))
        }
        

        if (task.reminderDateTime != null && task.reminderDateTime < now && !task.isCompleted) {
            return Result.failure(IllegalArgumentException("La fecha del recordatorio no puede ser anterior a la fecha actual para tareas pendientes"))
        }
        

        val existingTask = repository.getTaskById(task.id)
            ?: return Result.failure(IllegalArgumentException("La tarea no existe"))
        

        val updatedTask = task.copy(
            title = task.title.trim(),
            description = task.description?.trim(),
            updatedAt = now,

            completedAt = if (task.isCompleted && !existingTask.isCompleted) {
                now
            } else if (!task.isCompleted && existingTask.isCompleted) {
                null
            } else {
                task.completedAt
            }
        )
        
        val result = repository.updateTask(updatedTask)
        

        if (result.isSuccess) {

            notificationManager.cancelNotification(task.id)
            

            if (updatedTask.reminderDateTime != null && !updatedTask.isCompleted) {
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