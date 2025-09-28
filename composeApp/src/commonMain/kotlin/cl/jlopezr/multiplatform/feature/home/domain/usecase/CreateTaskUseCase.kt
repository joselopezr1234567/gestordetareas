package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Caso de uso para crear una nueva tarea
 * Incluye validaciones de negocio y generación de ID único
 */
class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para crear una nueva tarea
     * 
     * @param title Título de la tarea (obligatorio)
     * @param description Descripción de la tarea (opcional)
     * @param priority Prioridad de la tarea
     * @param dueDate Fecha límite (opcional)
     * @param reminderDateTime Fecha y hora del recordatorio (opcional)
     * @return Result con la tarea creada o error
     */
    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        dueDate: LocalDateTime? = null,
        reminderDateTime: LocalDateTime? = null
    ): Result<Task> {
        
        // Validaciones de negocio
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
        
        // Validar fecha límite
        if (dueDate != null && dueDate < now) {
            return Result.failure(IllegalArgumentException("La fecha límite no puede ser anterior a la fecha actual"))
        }
        
        // Validar recordatorio
        if (reminderDateTime != null && reminderDateTime < now) {
            return Result.failure(IllegalArgumentException("La fecha del recordatorio no puede ser anterior a la fecha actual"))
        }
        
        // Crear la tarea
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
        
        return repository.createTask(task)
    }
    
    /**
     * Genera un ID único para la tarea
     */
    private fun generateTaskId(): String {
        return "task_${Clock.System.now().toEpochMilliseconds()}_${(0..9999).random()}"
    }
}