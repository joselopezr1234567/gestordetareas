package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task


data class TaskDetailUiState(

    val isLoading: Boolean = false,
    

    val task: Task? = null,
    

    val navigateBack: Boolean = false,
    

    val error: String? = null
)