package cl.jlopezr.multiplatform.feature.home.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Entidad de dominio que representa una tarea
 * Contiene toda la información necesaria para gestionar tareas en la aplicación
 */
data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val completedAt: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null
) {
    /**
     * Verifica si la tarea está vencida
     */
    fun isOverdue(): Boolean {
        return dueDate?.let { due ->
            !isCompleted && due < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        } ?: false
    }

    /**
     * Obtiene el color asociado a la prioridad
     */
    fun getPriorityColor(): Long {
        return when (priority) {
            TaskPriority.HIGH -> 0xFFE53E3E // Rojo
            TaskPriority.MEDIUM -> 0xFFED8936 // Naranja
            TaskPriority.LOW -> 0xFF38A169 // Verde
        }
    }
}

/**
 * Enum que representa los niveles de prioridad de una tarea
 */
enum class TaskPriority(val value: Int, val displayName: String) {
    LOW(1, "Baja"),
    MEDIUM(2, "Media"),
    HIGH(3, "Alta");

    companion object {
        fun fromValue(value: Int): TaskPriority {
            return entries.find { it.value == value } ?: MEDIUM
        }
    }
}

/**
 * Enum que representa los filtros disponibles para las tareas
 */
enum class TaskFilter(val displayName: String) {
    ALL("Todas"),
    PENDING("𝒫𝑒𝓃𝒹𝒾𝑒𝓃𝓉𝑒𝓈"),
    COMPLETED("Completadas");
}

/**
 * Enum que representa los criterios de ordenación
 */
enum class TaskSortOrder(val displayName: String) {
    CREATED_DATE_DESC("ℳá𝓈 𝓇𝑒𝒸𝒾𝑒𝓃𝓉𝑒𝓈"),
    CREATED_DATE_ASC("Más antiguas"),
    DUE_DATE_ASC("Vencimiento ↑"),
    DUE_DATE_DESC("Vencimiento ↓"),
    PRIORITY_DESC("Prioridad ↓"),
    PRIORITY_ASC("Prioridad ↑"),
    TITLE_ASC("Título A-Z"),
    TITLE_DESC("Título Z-A"),
    COMPLETION_STATUS("Estado de completado");
}