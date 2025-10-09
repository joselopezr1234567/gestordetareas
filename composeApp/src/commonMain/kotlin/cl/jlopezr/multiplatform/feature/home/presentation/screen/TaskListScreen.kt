package cl.jlopezr.multiplatform.feature.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.presentation.components.TaskActionDialog
import cl.jlopezr.multiplatform.feature.home.presentation.components.TaskItem
import cl.jlopezr.multiplatform.feature.home.presentation.components.TaskSearchBar
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskListUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.state.TaskListUiState
import cl.jlopezr.multiplatform.feature.home.presentation.viewmodel.TaskListViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onNavigateToCreateTask: () -> Unit,
    onNavigateToEditTask: (String) -> Unit,
    onNavigateToTaskDetail: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onToggleDrawer: (() -> Unit)? = null,
    taskActionId: String? = null,
    viewTaskFromNotification: String? = null,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    

    var showTaskActionDialog by remember { mutableStateOf(taskActionId != null) }
    

    var showNotificationDialog by remember { mutableStateOf(false) }
    

    LaunchedEffect(Unit) {
        viewModel.onEvent(TaskListUiEvent.LoadTasks)
    }
    

    LaunchedEffect(viewTaskFromNotification) {
        if (viewTaskFromNotification != null) {
            delay(1000) // Delay de 1 segundo
            showNotificationDialog = true
        }
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(TaskListUiEvent.DismissError)
        }
    }
    
    Scaffold(
        modifier = modifier.background(Color.Black),
        containerColor = Color.Black,
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Text(
                            text = "𝕄𝕚𝕤 𝕥𝕒𝕣𝕖𝕒𝕤",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        ) 
                    },
                    navigationIcon = {
                        onToggleDrawer?.let { toggleDrawer ->
                            IconButton(onClick = toggleDrawer) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Configuraciones",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                )
            }
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
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

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
                

                when {
                    uiState.isLoading && uiState.tasks.isEmpty() -> {
                        // Estado de carga inicial
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    
                    uiState.tasks.isEmpty() && !uiState.isLoading -> {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
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
                                    color = Color.White
                                )
                            }
                        }
                    }
                    
                    else -> {

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
    

    if (showTaskActionDialog && taskActionId != null) {

        val task = uiState.tasks.find { it.id == taskActionId }
        
        if (task != null) {
            TaskActionDialog(
                task = task,
                isVisible = true,
                onCompleteTask = {
                    viewModel.onEvent(TaskListUiEvent.ToggleTaskCompletion(taskActionId))
                    showTaskActionDialog = false
                },
                onEditTask = {
                    onNavigateToEditTask(taskActionId)
                    showTaskActionDialog = false
                },
                onDismiss = {
                    showTaskActionDialog = false
                }
            )
        } else {

            showTaskActionDialog = false
        }
    }
    

    if (showNotificationDialog && viewTaskFromNotification != null) {

        val task = uiState.tasks.find { it.id == viewTaskFromNotification }
        
        if (task != null) {
            TaskActionDialog(
                task = task,
                isVisible = true,
                onCompleteTask = {
                    viewModel.onEvent(TaskListUiEvent.ToggleTaskCompletion(viewTaskFromNotification))
                    showNotificationDialog = false
                },
                onEditTask = {
                    onNavigateToEditTask(viewTaskFromNotification)
                    showNotificationDialog = false
                },
                onDismiss = {
                    showNotificationDialog = false
                }
            )
        } else {

            showNotificationDialog = false
        }
    }
}