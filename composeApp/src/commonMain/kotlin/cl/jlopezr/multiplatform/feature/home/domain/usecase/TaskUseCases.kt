package cl.jlopezr.multiplatform.feature.home.domain.usecase

/**
 * Conjunto de casos de uso relacionados con tareas
 * Facilita la inyección de dependencias y el uso en ViewModels
 */
data class TaskUseCases(
    val getFilteredTasks: GetFilteredTasksUseCase,
    val getTaskById: GetTaskByIdUseCase,
    val createTask: CreateTaskUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val toggleTaskCompletion: ToggleTaskCompletionUseCase,
    val getTaskStatistics: GetTaskStatisticsUseCase
)