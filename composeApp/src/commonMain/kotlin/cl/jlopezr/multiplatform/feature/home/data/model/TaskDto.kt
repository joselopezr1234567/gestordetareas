package cl.jlopezr.multiplatform.feature.home.data.model

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object para tareas
 * Se usa para serializar/deserializar tareas en DataStore
 */
@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: Int = TaskPriority.MEDIUM.value,
    val dueDate: String? = null, // ISO string format
    val isCompleted: Boolean = false,
    val createdAt: String, // ISO string format
    val updatedAt: String, // ISO string format
    val completedAt: String? = null, // ISO string format
    val reminderDateTime: String? = null // ISO string format
)

/**
 * Extensiones para convertir entre Task y TaskDto
 */
fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        priority = priority.value,
        dueDate = dueDate?.toString(),
        isCompleted = isCompleted,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        completedAt = completedAt?.toString(),
        reminderDateTime = reminderDateTime?.toString()
    )
}

fun TaskDto.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        priority = TaskPriority.fromValue(priority),
        dueDate = dueDate?.let { LocalDateTime.parse(it) },
        isCompleted = isCompleted,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt),
        completedAt = completedAt?.let { LocalDateTime.parse(it) },
        reminderDateTime = reminderDateTime?.let { LocalDateTime.parse(it) }
    )
}

/**
 * Lista de tareas serializable para DataStore
 */
@Serializable
data class TaskListDto(
    val tasks: List<TaskDto> = emptyList()
)

fun List<Task>.toDto(): TaskListDto {
    return TaskListDto(tasks = map { it.toDto() })
}

fun TaskListDto.toDomain(): List<Task> {
    return tasks.map { it.toDomain() }
}