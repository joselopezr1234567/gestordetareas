package cl.jlopezr.multiplatform.feature.statistics.presentation.event

import cl.jlopezr.multiplatform.feature.statistics.presentation.state.StatisticsPeriod


sealed class StatisticsUiEvent {

    data object LoadStatistics : StatisticsUiEvent()
    

    data object RefreshStatistics : StatisticsUiEvent()
    

    data class ChangePeriod(val period: StatisticsPeriod) : StatisticsUiEvent()
    

    data object ClearError : StatisticsUiEvent()
}