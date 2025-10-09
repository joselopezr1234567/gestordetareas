package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository


class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    

    suspend operator fun invoke(taskId: String): Task? {
        require(taskId.isNotBlank()) { "El ID de la tarea no puede estar vacío" }
        
        return taskRepository.getTaskById(taskId)
    }
}