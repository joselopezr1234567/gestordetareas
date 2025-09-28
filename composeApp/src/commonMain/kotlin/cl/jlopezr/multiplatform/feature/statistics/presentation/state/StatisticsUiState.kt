package cl.jlopezr.multiplatform.feature.statistics.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskStatistics

/**
 * Estado de la UI para la pantalla de estadísticas
 */
data class StatisticsUiState(
    val isLoading: Boolean = false,
    val statistics: TaskStatistics = TaskStatistics.empty(),
    val errorMessage: String? = null,
    val selectedPeriod: StatisticsPeriod = StatisticsPeriod.ALL_TIME
) {
    /**
     * Indica si hay datos para mostrar
     */
    val hasData: Boolean
        get() = statistics.totalTasks > 0
    
    /**
     * Indica si se debe mostrar el estado vacío
     */
    val showEmptyState: Boolean
        get() = !isLoading && !hasData && errorMessage == null
}

/**
 * Enum para los períodos de estadísticas
 */
enum class StatisticsPeriod(val displayName: String) {
    ALL_TIME("Todo el tiempo"),
    THIS_WEEK("Esta semana"),
    THIS_MONTH("Este mes"),
    LAST_MONTH("Mes pasado")
}