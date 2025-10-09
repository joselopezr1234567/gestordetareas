package cl.jlopezr.multiplatform.feature.settings.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.core.theme.ThemeMode
import cl.jlopezr.multiplatform.feature.settings.presentation.event.SettingsUiEvent
import cl.jlopezr.multiplatform.feature.settings.presentation.state.SettingsUiState
import cl.jlopezr.multiplatform.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Configuraciones",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                SettingsSection(title = "Apariencia") {
                    SettingsItem(
                        icon = Icons.Default.Palette,
                        title = "Tema",
                        subtitle = uiState.currentThemeDisplayName,
                        onClick = { viewModel.onEvent(SettingsUiEvent.ShowThemeDialog) }
                    )
                }
            }
            

            item {
                SettingsSection(title = "Notificaciones") {
                    SettingsSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones",
                        subtitle = "Recibir recordatorios de tareas",
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { 
                            viewModel.onEvent(SettingsUiEvent.ToggleNotifications(it))
                        }
                    )
                }
            }
            

            item {
                SettingsSection(title = "Gestión de Tareas") {
                    Column {
                        SettingsSwitchItem(
                            icon = Icons.Default.Delete,
                            title = "Auto-eliminar completadas",
                            subtitle = "Eliminar automáticamente tareas completadas después de ${uiState.autoDeleteDays} días",
                            checked = uiState.autoDeleteEnabled,
                            onCheckedChange = { 
                                viewModel.onEvent(SettingsUiEvent.ToggleAutoDelete(it))
                            }
                        )
                        
                        if (uiState.autoDeleteEnabled) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 48.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Días:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedTextField(
                                    value = uiState.autoDeleteDays.toString(),
                                    onValueChange = { value ->
                                        value.toIntOrNull()?.let { days: Int ->
                                            if (days > 0) {
                                                viewModel.onEvent(SettingsUiEvent.ChangeAutoDeleteDays(days))
                                            }
                                        }
                                    },
                                    modifier = Modifier.width(80.dp),
                                    singleLine = true
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SettingsItem(
                            icon = Icons.Default.Assignment,
                            title = "Prioridad por defecto",
                            subtitle = uiState.priorityDisplayName,
                            onClick = { /* TODO: Implementar selector de prioridad */ }
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SettingsSwitchItem(
                            icon = Icons.Default.Visibility,
                            title = "Mostrar tareas completadas",
                            subtitle = "Mostrar tareas completadas en la lista principal",
                            checked = uiState.showCompletedTasks,
                            onCheckedChange = { 
                                viewModel.onEvent(SettingsUiEvent.ToggleShowCompleted(it))
                            }
                        )
                    }
                }
            }
            

            item {
                SettingsSection(title = "Información") {
                    SettingsItem(
                        icon = Icons.Default.BarChart,
                        title = "Estadísticas",
                        subtitle = "Ver estadísticas de productividad",
                        onClick = onNavigateToStatistics
                    )
                }
            }
            

            item {
                SettingsSection(title = "Datos") {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.DeleteSweep,
                            title = "Eliminar tareas completadas",
                            subtitle = "Eliminar todas las tareas completadas",
                            onClick = { 
                                viewModel.onEvent(SettingsUiEvent.DeleteCompletedTasks)
                            },
                            isDestructive = true
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        SettingsItem(
                            icon = Icons.Default.Warning,
                            title = "Eliminar todos los datos",
                            subtitle = "Eliminar todas las tareas y configuraciones",
                            onClick = { 
                                viewModel.onEvent(SettingsUiEvent.ShowDeleteConfirmation)
                            },
                            isDestructive = true
                        )
                    }
                }
            }
            

            item {
                SettingsSection(title = "Acerca de") {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Información de la app",
                        subtitle = "Versión 1.0.0",
                        onClick = { 
                            viewModel.onEvent(SettingsUiEvent.ShowAboutDialog)
                        }
                    )
                }
            }
        }
    }
    

    if (uiState.showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = uiState.currentTheme,
            onThemeSelected = { theme ->
                viewModel.onEvent(SettingsUiEvent.ChangeTheme(theme))
                viewModel.onEvent(SettingsUiEvent.HideThemeDialog)
            },
            onDismiss = { viewModel.onEvent(SettingsUiEvent.HideThemeDialog) }
        )
    }
    
    if (uiState.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                viewModel.onEvent(SettingsUiEvent.DeleteAllData)
                viewModel.onEvent(SettingsUiEvent.HideDeleteConfirmation)
            },
            onDismiss = { viewModel.onEvent(SettingsUiEvent.HideDeleteConfirmation) }
        )
    }
    
    if (uiState.showAboutDialog) {
        AboutDialog(
            onDismiss = { viewModel.onEvent(SettingsUiEvent.HideAboutDialog) }
        )
    }
    

}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error 
                       else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDestructive) MaterialTheme.colorScheme.error 
                            else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar tema") },
        text = {
            Column {
                ThemeMode.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == theme,
                            onClick = { onThemeSelected(theme) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (theme) {
                                ThemeMode.LIGHT -> "Claro"
                                ThemeMode.DARK -> "Oscuro"
                                ThemeMode.SYSTEM -> "Sistema"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar todos los datos") },
        text = { 
            Text("Esta acción eliminará todas las tareas y configuraciones. Esta acción no se puede deshacer.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agenda de Tareas") },
        text = {
            Column {
                Text("Versión: 1.0.0")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Una aplicación multiplataforma para gestionar tus tareas diarias.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Desarrollado con Kotlin Multiplatform y Compose Multiplatform.")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}