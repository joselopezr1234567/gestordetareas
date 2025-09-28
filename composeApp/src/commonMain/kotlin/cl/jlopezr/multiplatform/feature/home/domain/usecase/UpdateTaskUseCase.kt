package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Caso de uso para actualizar una tarea existente
 * Incluye validaciones de negocio y actualización de timestamps
 */
class UpdateTaskUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para actualizar una tarea
     * 
     * @param task Tarea con los datos actualizados
     * @return Result con la tarea actualizada o error
     */
    suspend operator fun invoke(task: Task): Result<Task> {
        
        // Validaciones de negocio
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
        
        // Validar fecha límite
        if (task.dueDate != null && task.dueDate < now && !task.isCompleted) {
            return Result.failure(IllegalArgumentException("La fecha límite no puede ser anterior a la fecha actual para tareas pendientes"))
        }
        
        // Validar recordatorio
        if (task.reminderDateTime != null && task.reminderDateTime < now && !task.isCompleted) {
            return Result.failure(IllegalArgumentException("La fecha del recordatorio no puede ser anterior a la fecha actual para tareas pendientes"))
        }
        
        // Verificar que la tarea existe
        val existingTask = repository.getTaskById(task.id)
            ?: return Result.failure(IllegalArgumentException("La tarea no existe"))
        
        // Actualizar timestamps
        val updatedTask = task.copy(
            title = task.title.trim(),
            description = task.description?.trim(),
            updatedAt = now,
            // Si se está marcando como completada, establecer completedAt
            completedAt = if (task.isCompleted && !existingTask.isCompleted) {
                now
            } else if (!task.isCompleted && existingTask.isCompleted) {
                null
            } else {
                task.completedAt
            }
        )
        
        return repository.updateTask(updatedTask)
    }
}