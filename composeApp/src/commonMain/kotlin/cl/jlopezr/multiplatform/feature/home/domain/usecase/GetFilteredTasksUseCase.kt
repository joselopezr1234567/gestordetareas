package cl.jlopezr.multiplatform.feature.home.domain.usecase

import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow


class GetFilteredTasksUseCase(
    private val repository: TaskRepository
) {
    

    operator fun invoke(
        filter: TaskFilter = TaskFilter.ALL,
        sortOrder: TaskSortOrder = TaskSortOrder.DUE_DATE_ASC,
        searchQuery: String = ""
    ): Flow<List<Task>> {
        return repository.getFilteredTasks(filter, sortOrder, searchQuery)
    }
}