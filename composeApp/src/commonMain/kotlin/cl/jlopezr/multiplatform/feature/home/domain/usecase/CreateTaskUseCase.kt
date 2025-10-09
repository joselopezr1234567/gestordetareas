package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.core.notification.NotificationManager
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    private val notificationManager: NotificationManager by lazy {
        NotificationManagerFactory.create()
    }
    

    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        dueDate: LocalDateTime? = null,
        reminderDateTime: LocalDateTime? = null
    ): Result<Task> {
        

        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }
        
        if (title.length > 100) {
            return Result.failure(IllegalArgumentException("El título no puede exceder 100 caracteres"))
        }
        
        if (description != null && description.length > 500) {
            return Result.failure(IllegalArgumentException("La descripción no puede exceder 500 caracteres"))
        }
        
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        

        if (dueDate != null && dueDate < now) {
            return Result.failure(IllegalArgumentException("La fecha límite no puede ser anterior a la fecha actual"))
        }
        

        if (reminderDateTime != null && reminderDateTime < now) {
            return Result.failure(IllegalArgumentException("La fecha del recordatorio no puede ser anterior a la fecha actual"))
        }
        

        val task = Task(
            id = generateTaskId(),
            title = title.trim(),
            description = description?.trim(),
            priority = priority,
            dueDate = dueDate,
            isCompleted = false,
            createdAt = now,
            updatedAt = now,
            completedAt = null,
            reminderDateTime = reminderDateTime
        )
        
        val result = repository.createTask(task)
        

        if (result.isSuccess && reminderDateTime != null) {
            println("CreateTaskUseCase: Programando notificación para tarea ${task.id} en $reminderDateTime")
            val notificationResult = notificationManager.scheduleNotification(
                id = task.id,
                title = "Recordatorio de tarea",
                message = task.title,
                dateTime = reminderDateTime
            )
            if (notificationResult.isSuccess) {
                println("CreateTaskUseCase: Notificación programada exitosamente")
            } else {
                println("CreateTaskUseCase: Error al programar notificación: ${notificationResult.exceptionOrNull()}")
            }
        } else {
            println("CreateTaskUseCase: No se programó notificación - Success: ${result.isSuccess}, ReminderDateTime: $reminderDateTime")
        }
        
        return result
    }
    

    private fun generateTaskId(): String {
        return "task_${Clock.System.now().toEpochMilliseconds()}_${(0..9999).random()}"
    }
}