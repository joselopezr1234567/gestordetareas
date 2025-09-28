package cl.jlopezr.multiplatform.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.presentation.components.TaskItem
import cl.jlopezr.multiplatform.feature.home.presentation.components.TaskSearchBar
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskListUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.state.TaskListUiState
import cl.jlopezr.multiplatform.feature.home.presentation.viewmodel.TaskListViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Pantalla principal que muestra la lista de tareas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onNavigateToCreateTask: () -> Unit,
    onNavigateToEditTask: (String) -> Unit,
    onNavigateToTaskDetail: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onToggleDrawer: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Efectos para manejar eventos
    LaunchedEffect(Unit) {
        viewModel.onEvent(TaskListUiEvent.LoadTasks)
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(TaskListUiEvent.DismissError)
        }
    }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Mis Tareas",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                navigationIcon = {
                    onToggleDrawer?.let { toggleDrawer ->
                        IconButton(onClick = toggleDrawer) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuraciones"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateTask,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear nueva tarea"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                viewModel.onEvent(TaskListUiEvent.RefreshTasks)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Barra de búsqueda y filtros
                TaskSearchBar(
                    searchQuery = uiState.searchQuery,
                    currentFilter = uiState.currentFilter,
                    currentSortOrder = uiState.currentSortOrder,
                    onSearchQueryChange = { query ->
                        viewModel.onEvent(TaskListUiEvent.SearchQueryChanged(query))
                    },
                    onFilterChange = { filter ->
                        viewModel.onEvent(TaskListUiEvent.FilterChanged(filter))
                    },
                    onSortOrderChange = { sortOrder ->
                        viewModel.onEvent(TaskListUiEvent.SortOrderChanged(sortOrder))
                    }
                )
                
                // Lista de tareas
                when {
                    uiState.isLoading && uiState.tasks.isEmpty() -> {
                        // Estado de carga inicial
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    
                    uiState.tasks.isEmpty() && !uiState.isLoading -> {
                        // Estado vacío
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (uiState.searchQuery.isNotEmpty()) {
                                        "No se encontraron tareas que coincidan con tu búsqueda"
                                    } else {
                                        "No tienes tareas aún.\n¡Crea tu primera tarea!"
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    else -> {
                        // Lista de tareas
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = uiState.tasks,
                                key = { task -> task.id }
                            ) { task ->
                                TaskItem(
                                    task = task,
                                    onToggleCompletion = { taskId ->
                                        viewModel.onEvent(TaskListUiEvent.ToggleTaskCompletion(taskId))
                                    },
                                    onEdit = { taskId ->
                                        onNavigateToEditTask(taskId)
                                    },
                                    onDelete = { taskId ->
                                        viewModel.onEvent(TaskListUiEvent.DeleteTask(taskId))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}