package cl.jlopezr.multiplatform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
@Composable
private fun AppNavigation() {
    var navigationState by remember { 
        mutableStateOf(NavigationState(currentScreen = Screen.Splash)) 
    }
    
    when (navigationState.currentScreen) {
        Screen.Splash -> {
            SplashScreen(
                onNavigateToLogin = { 
                    navigationState = navigationState.copy(currentScreen = Screen.Login) 
                },
                onNavigateToHome = { 
                    navigationState = navigationState.copy(currentScreen = Screen.TaskList) 
                }
            )
        }
        
        Screen.Login -> {
            LoginScreen(
                onNavigateToHome = { 
                    navigationState = navigationState.copy(currentScreen = Screen.TaskList) 
                }
            )
        }
        
        Screen.TaskList -> {
            TaskListScreen(
                onNavigateToCreateTask = {
                    navigationState = navigationState.copy(
                        currentScreen = Screen.TaskForm,
                        taskId = null,
                        isEditMode = false
                    )
                },
                onNavigateToEditTask = { taskId ->
                    navigationState = navigationState.copy(
                        currentScreen = Screen.TaskForm,
                        taskId = taskId,
                        isEditMode = true
                    )
                },
                onNavigateToTaskDetail = { taskId ->
                    navigationState = navigationState.copy(
                        currentScreen = Screen.TaskDetail,
                        taskId = taskId
                    )
                },
                onNavigateToLogin = {
                    navigationState = navigationState.copy(currentScreen = Screen.Login)
                },
                onNavigateToSettings = {
                    navigationState = navigationState.copy(currentScreen = Screen.Settings)
                }
            )
        }
        
        Screen.TaskForm -> {
            TaskFormScreen(
                taskId = if (navigationState.isEditMode) navigationState.taskId else null,
                onNavigateBack = {
                    navigationState = navigationState.copy(
                        currentScreen = Screen.TaskList,
                        taskId = null,
                        isEditMode = false
                    )
                }
            )
        }
        
        Screen.TaskDetail -> {
            // Por ahora redirigimos a TaskList, se implementará en futuras iteraciones
            LaunchedEffect(Unit) {
                navigationState = navigationState.copy(
                    currentScreen = Screen.TaskList,
                    taskId = null
                )
            }
        }
        
        Screen.Settings -> {
            SettingsScreen(
                onNavigateBack = {
                    navigationState = navigationState.copy(currentScreen = Screen.TaskList)
                },
                onNavigateToStatistics = {
                    navigationState = navigationState.copy(currentScreen = Screen.Statistics)
                }
            )
        }
        
        Screen.Statistics -> {
            StatisticsScreen(
                onNavigateBack = {
                    navigationState = navigationState.copy(currentScreen = Screen.Settings)
                }
            )
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



/**
 * Enum para las pantallas de la aplicación
 */
private enum class Screen {
    Splash,
    Login,
    TaskList,
    TaskForm,
    TaskDetail,
    Settings,
    Statistics
}

/**
 * Data class para manejar parámetros de navegación
 */
private data class NavigationState(
    val currentScreen: Screen = Screen.Splash,
    val taskId: String? = null,
    val isEditMode: Boolean = false
)