package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener tareas filtradas y ordenadas
 * Implementa la lógica de negocio para filtrar y ordenar tareas
 */
class GetFilteredTasksUseCase(
    private val repository: TaskRepository
) {
    
    /**
     * Ejecuta el caso de uso para obtener tareas filtradas
     * 
     * @param filter Filtro a aplicar (todas, pendientes, completadas)
     * @param sortOrder Criterio de ordenación
     * @param searchQuery Texto de búsqueda (opcional)
     * @return Flow con la lista de tareas filtradas y ordenadas
     */
    operator fun invoke(
        filter: TaskFilter = TaskFilter.ALL,
        sortOrder: TaskSortOrder = TaskSortOrder.DUE_DATE_ASC,
        searchQuery: String = ""
    ): Flow<List<Task>> {
        return repository.getFilteredTasks(filter, sortOrder, searchQuery)
    }
}