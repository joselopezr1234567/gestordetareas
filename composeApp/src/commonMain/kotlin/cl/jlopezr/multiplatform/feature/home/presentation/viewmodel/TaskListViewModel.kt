package cl.jlopezr.multiplatform.feature.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.feature.home.domain.usecase.TaskUseCases
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskListUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.state.TaskListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class TaskListViewModel(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(TaskListUiState())
        private set
    
    private var searchJob: Job? = null
    private var loadTasksJob: Job? = null
    
    init {
        loadTasks()
    }
    

    fun onEvent(event: TaskListUiEvent) {
        when (event) {
            is TaskListUiEvent.LoadTasks -> loadTasks()
            is TaskListUiEvent.RefreshTasks -> refreshTasks()
            is TaskListUiEvent.SearchQueryChanged -> updateSearchQuery(event.query)
            is TaskListUiEvent.ToggleSearch -> toggleSearch()
            is TaskListUiEvent.ClearSearch -> clearSearch()
            is TaskListUiEvent.FilterChanged -> updateFilter(event.filter)
            is TaskListUiEvent.ShowFilterDialog -> showFilterDialog()
            is TaskListUiEvent.HideFilterDialog -> hideFilterDialog()
            is TaskListUiEvent.SortOrderChanged -> updateSortOrder(event.sortOrder)
            is TaskListUiEvent.ShowSortDialog -> showSortDialog()
            is TaskListUiEvent.HideSortDialog -> hideSortDialog()
            is TaskListUiEvent.ToggleTaskCompletion -> toggleTaskCompletion(event.taskId)
            is TaskListUiEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskListUiEvent.ShowDeleteConfirmation -> showDeleteConfirmation(event.taskId)
            is TaskListUiEvent.HideDeleteConfirmation -> hideDeleteConfirmation()
            is TaskListUiEvent.ConfirmDeleteTask -> confirmDeleteTask()
            is TaskListUiEvent.DeleteCompletedTasks -> deleteCompletedTasks()
            is TaskListUiEvent.DismissError -> dismissError()
            is TaskListUiEvent.OnSwipeRefresh -> onSwipeRefresh()

            else -> { /* Eventos de navegación manejados en la UI */ }
        }
    }
    

    private fun loadTasks() {
        loadTasksJob?.cancel()
        
        try {
            loadTasksJob = taskUseCases.getFilteredTasks(
                filter = uiState.currentFilter,
                sortOrder = uiState.currentSortOrder,
                searchQuery = uiState.searchQuery
            )
                .onEach { tasks ->
                    try {
                        uiState = uiState.copy(
                            tasks = tasks,
                            isLoading = false,
                            error = null,
                            isRefreshing = false
                        )
                    } catch (e: Exception) {
                        println("Error updating UI state: ${e.message}")
                        uiState = uiState.copy(
                            isLoading = false,
                            error = "Error al actualizar la interfaz: ${e.message}",
                            isRefreshing = false
                        )
                    }
                }
                .catch { error ->
                    println("Error in task flow: ${error.message}")
                    uiState = uiState.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconocido al cargar tareas",
                        isRefreshing = false
                    )
                }
                .launchIn(viewModelScope)
            
            if (!uiState.isRefreshing) {
                uiState = uiState.copy(isLoading = true, error = null)
            }
        } catch (e: Exception) {
            println("Error initializing task loading: ${e.message}")
            uiState = uiState.copy(
                isLoading = false,
                error = "Error al inicializar la carga de tareas: ${e.message}",
                isRefreshing = false
            )
        }
    }
    

    private fun refreshTasks() {
        uiState = uiState.copy(isRefreshing = true)
        loadTasks()
    }
    

    private fun updateSearchQuery(query: String) {
        uiState = uiState.copy(searchQuery = query)
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce de 300ms
            loadTasks()
        }
    }
    

    private fun toggleSearch() {
        uiState = uiState.copy(
            isSearchActive = !uiState.isSearchActive,
            searchQuery = if (uiState.isSearchActive) "" else uiState.searchQuery
        )
        if (!uiState.isSearchActive && uiState.searchQuery.isNotBlank()) {
            loadTasks()
        }
    }
    

    private fun clearSearch() {
        uiState = uiState.copy(
            searchQuery = "",
            isSearchActive = false
        )
        loadTasks()
    }
    

    private fun updateFilter(filter: cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter) {
        uiState = uiState.copy(
            currentFilter = filter,
            showFilterDialog = false
        )
        loadTasks()
    }
    

    private fun showFilterDialog() {
        uiState = uiState.copy(showFilterDialog = true)
    }
    

    private fun hideFilterDialog() {
        uiState = uiState.copy(showFilterDialog = false)
    }
    

    private fun updateSortOrder(sortOrder: cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder) {
        uiState = uiState.copy(
            currentSortOrder = sortOrder,
            showSortDialog = false
        )
        loadTasks()
    }
    

    private fun showSortDialog() {
        uiState = uiState.copy(showSortDialog = true)
    }
    

    private fun hideSortDialog() {
        uiState = uiState.copy(showSortDialog = false)
    }
    

    private fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            taskUseCases.toggleTaskCompletion(taskId)
                .onSuccess {

                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        error = error.message ?: "Error al actualizar la tarea"
                    )
                }
        }
    }
    

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskUseCases.deleteTask(taskId)
                .onSuccess {
                    // Las tareas se actualizan automáticamente por el Flow
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        error = error.message ?: "Error al eliminar la tarea"
                    )
                }
        }
    }
    

    private fun showDeleteConfirmation(taskId: String) {
        uiState = uiState.copy(
            selectedTaskId = taskId,
            showDeleteConfirmation = true
        )
    }
    

    private fun hideDeleteConfirmation() {
        uiState = uiState.copy(
            selectedTaskId = null,
            showDeleteConfirmation = false
        )
    }
    

    private fun confirmDeleteTask() {
        uiState.selectedTaskId?.let { taskId ->
            deleteTask(taskId)
            hideDeleteConfirmation()
        }
    }
    

    private fun deleteCompletedTasks() {
        viewModelScope.launch {
            try {
                // TODO: Implementar eliminación de tareas completadas

                uiState = uiState.copy(
                    error = "Función de eliminar tareas completadas no implementada aún"
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error al eliminar las tareas completadas"
                )
            }
        }
    }
    

    private fun dismissError() {
        uiState = uiState.copy(error = null)
    }
    

    private fun onSwipeRefresh() {
        refreshTasks()
    }
}