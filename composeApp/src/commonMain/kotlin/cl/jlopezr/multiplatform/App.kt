package cl.jlopezr.multiplatform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import cl.jlopezr.multiplatform.core.theme.AgendaTareasTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.di.appModule
import cl.jlopezr.multiplatform.feature.splash.ui.SplashScreen
import cl.jlopezr.multiplatform.feature.login.presentation.LoginScreen
import cl.jlopezr.multiplatform.feature.home.presentation.screen.TaskListScreen
import cl.jlopezr.multiplatform.feature.home.presentation.screen.TaskFormScreen
import cl.jlopezr.multiplatform.feature.settings.presentation.screen.SettingsScreen
import cl.jlopezr.multiplatform.feature.statistics.presentation.screen.StatisticsScreen
import cl.jlopezr.multiplatform.feature.drawer.ui.AppDrawer
import cl.jlopezr.multiplatform.core.navigation.Screen
import cl.jlopezr.multiplatform.core.navigation.NavigationState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

/**
 * Aplicación principal con Clean Architecture
 * Integra Koin para inyección de dependencias y navegación básica
 */
@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AgendaTareasTheme {
            AppNavigation()
        }
    }
}

/**
 * Navegación principal de la aplicación
 * Maneja la navegación entre todas las pantallas de la aplicación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavigation() {
    var navigationState by remember { 
        mutableStateOf(NavigationState(currentScreen = Screen.Splash)) 
    }
    
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Función para alternar el drawer
    val toggleDrawer: () -> Unit = {
        scope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }
    
    // Función para manejar navegación desde el drawer
    val handleDrawerNavigation: (Screen) -> Unit = { screen ->
        scope.launch {
            drawerState.close()
            navigationState = navigationState.copy(currentScreen = screen)
        }
    }
    
    // Función para manejar logout
    val handleLogout: () -> Unit = {
        scope.launch {
            drawerState.close()
            navigationState = navigationState.copy(currentScreen = Screen.Login)
        }
    }
    
    // Solo mostrar el drawer en pantallas que lo necesiten
    val showDrawer = navigationState.currentScreen in listOf(
        Screen.TaskList, 
        Screen.Settings, 
        Screen.Statistics
    )
    
    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentScreen = navigationState.currentScreen,
                    onNavigate = handleDrawerNavigation,
                    onLogout = handleLogout
                )
            }
        ) {
            AppContent(
                navigationState = navigationState,
                onNavigationStateChange = { navigationState = it },
                onToggleDrawer = toggleDrawer
            )
        }
    } else {
        AppContent(
            navigationState = navigationState,
            onNavigationStateChange = { navigationState = it },
            onToggleDrawer = null
        )
    }
}

/**
 * Contenido principal de la aplicación
 */
@Composable
private fun AppContent(
    navigationState: NavigationState,
    onNavigationStateChange: (NavigationState) -> Unit,
    onToggleDrawer: (() -> Unit)?
) {
    
    when (navigationState.currentScreen) {
        Screen.Splash -> {
            SplashScreen(
                onNavigateToLogin = { 
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.Login))
                },
                onNavigateToHome = { 
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.TaskList))
                }
            )
        }
        
        Screen.Login -> {
            LoginScreen(
                onNavigateToHome = { 
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.TaskList))
                }
            )
        }
        
        Screen.TaskList -> {
            TaskListScreen(
                onNavigateToCreateTask = {
                    onNavigationStateChange(navigationState.copy(
                        currentScreen = Screen.TaskForm,
                        taskId = null,
                        isEditMode = false
                    ))
                },
                onNavigateToEditTask = { taskId ->
                    onNavigationStateChange(navigationState.copy(
                        currentScreen = Screen.TaskForm,
                        taskId = taskId,
                        isEditMode = true
                    ))
                },
                onNavigateToTaskDetail = { taskId ->
                    onNavigationStateChange(navigationState.copy(
                        currentScreen = Screen.TaskDetail,
                        taskId = taskId
                    ))
                },
                onNavigateToLogin = {
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.Login))
                },
                onNavigateToSettings = {
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.Settings))
                },
                onToggleDrawer = onToggleDrawer
            )
        }
        
        Screen.TaskForm -> {
            TaskFormScreen(
                taskId = if (navigationState.isEditMode) navigationState.taskId else null,
                onNavigateBack = {
                    onNavigationStateChange(navigationState.copy(
                        currentScreen = Screen.TaskList,
                        taskId = null,
                        isEditMode = false
                    ))
                }
            )
        }
        
        Screen.TaskDetail -> {
            // Por ahora redirigimos a TaskList, se implementará en futuras iteraciones
            LaunchedEffect(Unit) {
                onNavigationStateChange(navigationState.copy(
                    currentScreen = Screen.TaskList,
                    taskId = null
                ))
            }
        }
        
        Screen.Settings -> {
            SettingsScreen(
                onNavigateBack = {
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.TaskList))
                },
                onNavigateToStatistics = {
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.Statistics))
                }
            )
        }
        
        Screen.Statistics -> {
            StatisticsScreen(
                onNavigateBack = {
                    onNavigationStateChange(navigationState.copy(currentScreen = Screen.Settings))
                }
            )
        }
        
        Screen.Logout -> {
            // El logout se maneja directamente en el drawer, redirigimos a Login
            LaunchedEffect(Unit) {
                onNavigationStateChange(navigationState.copy(currentScreen = Screen.Login))
            }
        }
    }
}

/**
 * Pantalla placeholder para Login
 * Se implementará completamente en futuras iteraciones
 */
@Composable
private fun LoginPlaceholderScreen(
    onNavigateToHome: () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(
                text = "🔐 Pantalla de Login",
                style = MaterialTheme.typography.headlineMedium
            )
            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            androidx.compose.material3.Text(
                text = "Esta pantalla se implementará próximamente",
                style = MaterialTheme.typography.bodyMedium
            )
            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
            androidx.compose.material3.Button(
                onClick = onNavigateToHome
            ) {
                androidx.compose.material3.Text("Ir a Home (Demo)")
            }
        }
    }
}