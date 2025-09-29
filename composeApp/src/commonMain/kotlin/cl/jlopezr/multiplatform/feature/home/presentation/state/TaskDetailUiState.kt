package cl.jlopezr.multiplatform.feature.home.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.model.Task

/**
 * Estado UI para la pantalla de detalle de tarea
 * Implementa el patrón UDF/MVI para manejo de estado unidireccional
 */
data class TaskDetailUiState(
    // Estados de carga
    val isLoading: Boolean = false,
    
    // Datos
    val task: Task? = null,
    
    // Estados de navegación
    val navigateBack: Boolean = false,
    
    // Estados de error
    val error: String? = null
)