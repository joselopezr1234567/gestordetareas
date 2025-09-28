package cl.jlopezr.multiplatform.core.navigation

/**
 * Enum para las pantallas de la aplicación
 */
enum class Screen {
    Splash,
    Login,
    TaskList,
    TaskForm,
    TaskDetail,
    Settings,
    Statistics,
    Logout
}

/**
 * Data class para manejar parámetros de navegación
 */
data class NavigationState(
    val currentScreen: Screen = Screen.Splash,
    val taskId: String? = null,
    val isEditMode: Boolean = false
)