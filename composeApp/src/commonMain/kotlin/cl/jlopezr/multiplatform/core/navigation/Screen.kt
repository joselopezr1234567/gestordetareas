package cl.jlopezr.multiplatform.core.navigation


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


data class NavigationState(
    val currentScreen: Screen = Screen.Splash,
    val taskId: String? = null,
    val isEditMode: Boolean = false
)