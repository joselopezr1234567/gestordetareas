package cl.jlopezr.multiplatform.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import cl.jlopezr.multiplatform.feature.home.presentation.event.TaskFormUiEvent
import cl.jlopezr.multiplatform.feature.home.presentation.viewmodel.TaskFormViewModel
import cl.jlopezr.multiplatform.feature.home.presentation.components.DatePickerDialog
import cl.jlopezr.multiplatform.feature.home.presentation.components.TimePickerDialog
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    taskId: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskFormViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    
    var showPriorityDropdown by remember { mutableStateOf(false) }
    
    val isEditMode = taskId != null
    

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.onEvent(TaskFormUiEvent.LoadTask(taskId))
        }
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(TaskFormUiEvent.DismissError)
        }
    }
    
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onNavigateBack()
            viewModel.onEvent(TaskFormUiEvent.OnNavigationHandled)
        }
    }
    
    LaunchedEffect(uiState.taskSaved) {
        if (uiState.taskSaved) {
            onNavigateBack()
            viewModel.onEvent(TaskFormUiEvent.OnTaskSavedHandled)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (isEditMode) "Editar Tarea" else "Nueva Tarea",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.onEvent(TaskFormUiEvent.TitleChanged(it)) },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.titleError != null,
                    supportingText = uiState.titleError?.let { { Text(it) } },
                    singleLine = true
                )
                

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.onEvent(TaskFormUiEvent.DescriptionChanged(it)) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.descriptionError != null,
                    supportingText = uiState.descriptionError?.let { { Text(it) } },
                    minLines = 3,
                    maxLines = 5
                )
                

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Prioridad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Box {
                            OutlinedButton(
                                onClick = { showPriorityDropdown = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(getPriorityDisplayName(uiState.priority))
                            }
                            
                            DropdownMenu(
                                expanded = showPriorityDropdown,
                                onDismissRequest = { showPriorityDropdown = false }
                            ) {
                                TaskPriority.values().forEach { priority ->
                                    DropdownMenuItem(
                                        text = { Text(getPriorityDisplayName(priority)) },
                                        onClick = {
                                            viewModel.onEvent(TaskFormUiEvent.PriorityChanged(priority))
                                            showPriorityDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fecha de vencimiento",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            
                            if (uiState.dueDate != null) {
                                TextButton(
                                    onClick = { viewModel.onEvent(TaskFormUiEvent.ClearDueDate) }
                                ) {
                                    Text("Quitar")
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = { viewModel.onEvent(TaskFormUiEvent.ShowDatePicker) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.dueDate != null) {
                                    "${uiState.dueDate!!.date} - ${uiState.dueDate!!.hour.toString().padStart(2, '0')}:${uiState.dueDate!!.minute.toString().padStart(2, '0')}"
                                } else {
                                    "Seleccionar fecha y hora"
                                }
                            )
                        }
                        
                        uiState.dueDateError?.let { error ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        

                        if (uiState.dueDate != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Programar recordatorio",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Recibir notificación en la fecha y hora seleccionada",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Switch(
                                    checked = uiState.reminderDateTime != null,
                                    onCheckedChange = { enabled ->
                                        if (enabled) {
                                            // Activar recordatorio con la misma fecha y hora de vencimiento
                                            viewModel.onEvent(TaskFormUiEvent.ReminderDateTimeChanged(uiState.dueDate))
                                        } else {
                                            // Desactivar recordatorio
                                            viewModel.onEvent(TaskFormUiEvent.ClearReminder)
                                        }
                                    }
                                )
                            }
                            
                            uiState.reminderError?.let { error ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = { viewModel.onEvent(TaskFormUiEvent.SaveTask) },
                        modifier = Modifier.weight(1f),
                        enabled = uiState.canSave && !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(4.dp)
                            )
                        } else {
                            Text(if (isEditMode) "Actualizar" else "Crear")
                        }
                    }
                }
            }
        }
        

        if (uiState.showDatePicker) {
            TimePickerDialog(
                onDateTimeSelected = { dateTime ->
                    viewModel.onEvent(TaskFormUiEvent.DueDateChanged(dateTime))
                },
                onDismiss = {
                    viewModel.onEvent(TaskFormUiEvent.HideDatePicker)
                },
                initialDateTime = uiState.dueDate
            )
        }
    }
}


private fun getPriorityDisplayName(priority: TaskPriority): String {
    return when (priority) {
        TaskPriority.HIGH -> "Alta"
        TaskPriority.MEDIUM -> "Media"
        TaskPriority.LOW -> "Baja"
    }
}