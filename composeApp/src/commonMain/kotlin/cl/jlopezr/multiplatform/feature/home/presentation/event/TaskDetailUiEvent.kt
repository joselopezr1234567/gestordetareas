package cl.jlopezr.multiplatform.feature.home.presentation.event


sealed class TaskDetailUiEvent {

    data class LoadTask(val taskId: String) : TaskDetailUiEvent()
    

    data object ToggleTaskCompletion : TaskDetailUiEvent()
    

    data object OnNavigationHandled : TaskDetailUiEvent()
    

    data object DismissError : TaskDetailUiEvent()
}