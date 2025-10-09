package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskStatistics


class GetTaskStatisticsUseCase(
    private val repository: TaskRepository
) {
    

    suspend operator fun invoke(): TaskStatistics {
        return repository.getTaskStatistics()
    }
}