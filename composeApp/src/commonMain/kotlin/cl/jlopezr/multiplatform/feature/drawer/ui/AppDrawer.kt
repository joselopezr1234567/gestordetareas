package cl.jlopezr.multiplatform.feature.drawer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.core.navigation.Screen
import cl.jlopezr.multiplatform.feature.drawer.presentation.AppDrawerViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Menú lateral de la aplicación
 */
@Composable
fun AppDrawer(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppDrawerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Mostrar snackbar de error si hay alguno
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            // Aquí podrías mostrar un snackbar o toast
            viewModel.clearError()
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header del drawer
        DrawerHeader()
        
        Divider()
        
        // Items de navegación
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(drawerItems) { item ->
                DrawerItem(
                    item = item,
                    isSelected = currentScreen == item.screen,
                    isLoading = item.screen == Screen.Logout && uiState.isLoggingOut,
                    onClick = {
                        if (item.screen == Screen.Logout) {
                            viewModel.logout(onLogout)
                        } else {
                            onNavigate(item.screen)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Header del drawer con información del usuario
 */
@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Avatar del usuario
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nombre del usuario
            Text(
                text = "Usuario",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            
            // Email del usuario
            Text(
                text = "usuario@ejemplo.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Item individual del drawer
 */
@Composable
private fun DrawerItem(
    item: DrawerItemData,
    isSelected: Boolean,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading) { onClick() }
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Datos de un item del drawer
 */
data class DrawerItemData(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

/**
 * Lista de items del drawer
 */
private val drawerItems = listOf(
    DrawerItemData(
        title = "Mis Tareas",
        icon = Icons.Default.List,
        screen = Screen.TaskList
    ),
    DrawerItemData(
        title = "Estadísticas",
        icon = Icons.Default.BarChart,
        screen = Screen.Statistics
    ),
    DrawerItemData(
        title = "Configuración",
        icon = Icons.Default.Settings,
        screen = Screen.Settings
    ),
    DrawerItemData(
        title = "Cerrar Sesión",
        icon = Icons.Default.ExitToApp,
        screen = Screen.Logout
    )
)