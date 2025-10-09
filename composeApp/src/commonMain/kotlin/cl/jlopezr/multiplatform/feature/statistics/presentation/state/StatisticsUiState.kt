package cl.jlopezr.multiplatform.feature.statistics.presentation.state

import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskStatistics


data class StatisticsUiState(
    val isLoading: Boolean = false,
    val statistics: TaskStatistics = TaskStatistics.empty(),
    val errorMessage: String? = null,
    val selectedPeriod: StatisticsPeriod = StatisticsPeriod.ALL_TIME
) {

    val hasData: Boolean
        get() = statistics.totalTasks > 0
    

    val showEmptyState: Boolean
        get() = !isLoading && !hasData && errorMessage == null
}


enum class StatisticsPeriod(val displayName: String) {
    ALL_TIME("Todo el tiempo"),
    THIS_WEEK("Esta semana"),
    THIS_MONTH("Este mes"),
    LAST_MONTH("Mes pasado")
}