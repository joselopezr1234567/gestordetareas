package cl.jlopezr.multiplatform.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentScreen: String,
    userEmail: String? = null,
    userName: String? = null,
    onNavigateToTasks: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header del drawer con información del usuario
            DrawerHeader(
                userEmail = userEmail,
                userName = userName
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            

            DrawerNavigationItem(
                icon = Icons.Default.Assignment,
                label = "Mis Tareas",
                isSelected = currentScreen == "TaskList",
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onNavigateToTasks()
                    }
                }
            )
            
            DrawerNavigationItem(
                icon = Icons.Default.TrendingUp,
                label = "Estadísticas",
                isSelected = currentScreen == "Statistics",
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onNavigateToStatistics()
                    }
                }
            )
            
            DrawerNavigationItem(
                icon = Icons.Default.Settings,
                label = "Configuración",
                isSelected = currentScreen == "Settings",
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onNavigateToSettings()
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            

            DrawerNavigationItem(
                icon = Icons.Default.ExitToApp,
                label = "Cerrar Sesión",
                isSelected = false,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onLogout()
                    }
                },
                isLogout = true
            )
        }
    }
}

/**
 * Header del drawer con información del usuario
 */
@Composable
private fun DrawerHeader(
    userEmail: String?,
    userName: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Usuario",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = userName ?: "Usuario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (userEmail != null) {
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Item de navegación del drawer
 */
@Composable
private fun DrawerNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isLogout: Boolean = false,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isLogout) {
                    MaterialTheme.colorScheme.error
                } else if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        },
        label = {
            Text(
                text = label,
                color = if (isLogout) {
                    MaterialTheme.colorScheme.error
                } else if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        },
        selected = isSelected && !isLogout,
        onClick = onClick,
        modifier = modifier,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = if (isLogout) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            }
        )
    )
}