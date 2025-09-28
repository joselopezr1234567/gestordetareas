package cl.jlopezr.multiplatform.feature.statistics.presentation.event

import cl.jlopezr.multiplatform.feature.statistics.presentation.state.StatisticsPeriod

/**
 * Eventos de la UI para la pantalla de estadísticas
 */
sealed class StatisticsUiEvent {
    /**
     * Cargar estadísticas
     */
    data object LoadStatistics : StatisticsUiEvent()
    
    /**
     * Refrescar estadísticas
     */
    data object RefreshStatistics : StatisticsUiEvent()
    
    /**
     * Cambiar período de estadísticas
     */
    data class ChangePeriod(val period: StatisticsPeriod) : StatisticsUiEvent()
    
    /**
     * Limpiar error
     */
    data object ClearError : StatisticsUiEvent()
}