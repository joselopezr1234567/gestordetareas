package cl.jlopezr.multiplatform.feature.home.domain.usecase


data class TaskUseCases(
    val getFilteredTasks: GetFilteredTasksUseCase,
    val getTaskById: GetTaskByIdUseCase,
    val createTask: CreateTaskUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val toggleTaskCompletion: ToggleTaskCompletionUseCase,
    val getTaskStatistics: GetTaskStatisticsUseCase
)