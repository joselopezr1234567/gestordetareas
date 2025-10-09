package cl.jlopezr.multiplatform.feature.statistics.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.feature.home.domain.usecase.GetTaskStatisticsUseCase
import cl.jlopezr.multiplatform.feature.statistics.presentation.event.StatisticsUiEvent
import cl.jlopezr.multiplatform.feature.statistics.presentation.state.StatisticsUiState
import cl.jlopezr.multiplatform.feature.statistics.presentation.state.StatisticsPeriod
import kotlinx.coroutines.launch


class StatisticsViewModel(
    private val getTaskStatisticsUseCase: GetTaskStatisticsUseCase
) : ViewModel() {
    
    var uiState by mutableStateOf(StatisticsUiState())
        private set
    

    fun onEvent(event: StatisticsUiEvent) {
        when (event) {
            is StatisticsUiEvent.LoadStatistics -> loadStatistics()
            is StatisticsUiEvent.RefreshStatistics -> refreshStatistics()
            is StatisticsUiEvent.ChangePeriod -> changePeriod(event.period)
            is StatisticsUiEvent.ClearError -> clearError()
        }
    }
    

    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, errorMessage = null)
                val statistics = getTaskStatisticsUseCase()
                uiState = uiState.copy(
                    isLoading = false,
                    statistics = statistics
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar estadísticas: ${e.message}"
                )
            }
        }
    }
    

    private fun refreshStatistics() {
        loadStatistics()
    }
    

    private fun changePeriod(period: StatisticsPeriod) {
        uiState = uiState.copy(selectedPeriod = period)

        loadStatistics()
    }
    

    private fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }
}