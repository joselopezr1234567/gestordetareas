package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskStatistics

/**
 * Caso de uso para obtener estadísticas de las tareas
 * Proporciona métricas útiles para la pantalla de estadísticas
 */
class GetTaskStatisticsUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para obtener estadísticas
     * 
     * @return TaskStatistics con todas las métricas calculadas
     */
    suspend operator fun invoke(): TaskStatistics {
        return repository.getTaskStatistics()
    }
}