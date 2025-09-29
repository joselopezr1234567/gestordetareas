package cl.jlopezr.multiplatform.feature.home.presentation.event

/**
 * Eventos UI para la pantalla de detalle de tarea
 * Define todas las acciones que el usuario puede realizar en la pantalla
 */
sealed class TaskDetailUiEvent {
    // Eventos de carga
    data class LoadTask(val taskId: String) : TaskDetailUiEvent()
    
    // Eventos de acciones
    data object ToggleTaskCompletion : TaskDetailUiEvent()
    
    // Eventos de navegación
    data object OnNavigationHandled : TaskDetailUiEvent()
    
    // Eventos de error
    data object DismissError : TaskDetailUiEvent()
}