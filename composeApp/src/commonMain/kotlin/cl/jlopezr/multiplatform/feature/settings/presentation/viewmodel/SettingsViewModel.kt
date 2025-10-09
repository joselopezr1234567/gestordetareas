package cl.jlopezr.multiplatform.feature.settings.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.core.theme.ThemeManager
import cl.jlopezr.multiplatform.core.theme.ThemeMode
import cl.jlopezr.multiplatform.feature.home.domain.usecase.TaskUseCases
import cl.jlopezr.multiplatform.feature.settings.presentation.event.SettingsUiEvent
import cl.jlopezr.multiplatform.feature.settings.presentation.state.SettingsUiState
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val themeManager: ThemeManager,
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(SettingsUiState())
        private set
    
    init {
        loadSettings()
    }
    

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.LoadSettings -> loadSettings()
            is SettingsUiEvent.SaveSettings -> saveSettings()
            is SettingsUiEvent.ChangeTheme -> changeTheme(event.theme)
            is SettingsUiEvent.ShowThemeDialog -> showThemeDialog()
            is SettingsUiEvent.HideThemeDialog -> hideThemeDialog()
            is SettingsUiEvent.ToggleNotifications -> toggleNotifications(event.enabled)
            is SettingsUiEvent.ToggleAutoDelete -> toggleAutoDelete(event.enabled)
            is SettingsUiEvent.ChangeAutoDeleteDays -> changeAutoDeleteDays(event.days)
            is SettingsUiEvent.ChangeDefaultPriority -> changeDefaultPriority(event.priority)
            is SettingsUiEvent.ToggleShowCompleted -> toggleShowCompleted(event.show)
            is SettingsUiEvent.ShowDeleteConfirmation -> showDeleteConfirmation()
            is SettingsUiEvent.HideDeleteConfirmation -> hideDeleteConfirmation()
            is SettingsUiEvent.DeleteAllData -> deleteAllData()
            is SettingsUiEvent.DeleteCompletedTasks -> deleteCompletedTasks()
            is SettingsUiEvent.ShowAboutDialog -> showAboutDialog()
            is SettingsUiEvent.HideAboutDialog -> hideAboutDialog()
            is SettingsUiEvent.ClearError -> clearError()
            is SettingsUiEvent.NavigateBack -> navigateBack()
            is SettingsUiEvent.NavigateToStatistics -> navigateToStatistics()
        }
    }
    
    e fun loadSettings() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                
                // Cargar configuraciones desde el ThemeManager
                themeManager.currentTheme.collect { theme ->
                    uiState = uiState.copy(
                        currentTheme = theme,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar configuraciones: ${e.message}"
                )
            }
        }
    }
    

    private fun saveSettings() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                
                // Aquí se implementaría la persistencia de configuraciones
                // Por ahora solo simulamos el guardado
                
                uiState = uiState.copy(isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al guardar configuraciones: ${e.message}"
                )
            }
        }
    }
    

    private fun changeTheme(theme: ThemeMode) {
        themeManager.setTheme(theme)
        uiState = uiState.copy(
            currentTheme = theme,
            showThemeDialog = false
        )
    }
    

    private fun showThemeDialog() {
        uiState = uiState.copy(showThemeDialog = true)
    }
    

    private fun hideThemeDialog() {
        uiState = uiState.copy(showThemeDialog = false)
    }
    

    private fun toggleNotifications(enabled: Boolean) {
        uiState = uiState.copy(notificationsEnabled = enabled)
    }
    

    private fun toggleAutoDelete(enabled: Boolean) {
        uiState = uiState.copy(autoDeleteEnabled = enabled)
    }
    

    private fun changeAutoDeleteDays(days: Int) {
        uiState = uiState.copy(autoDeleteDays = days)
    }
    

    private fun changeDefaultPriority(priority: String) {
        uiState = uiState.copy(defaultPriority = priority)
    }
    

    private fun toggleShowCompleted(show: Boolean) {
        uiState = uiState.copy(showCompletedTasks = show)
    }
    

    private fun showDeleteConfirmation() {
        uiState = uiState.copy(showDeleteConfirmation = true)
    }
    

    private fun hideDeleteConfirmation() {
        uiState = uiState.copy(showDeleteConfirmation = false)
    }
    

    private fun deleteAllData() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                

                
                uiState = uiState.copy(
                    isLoading = false,
                    showDeleteConfirmation = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al eliminar datos: ${e.message}"
                )
            }
        }
    }
    

    private fun deleteCompletedTasks() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                

                
                uiState = uiState.copy(isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al eliminar tareas completadas: ${e.message}"
                )
            }
        }
    }
    

    private fun showAboutDialog() {
        uiState = uiState.copy(showAboutDialog = true)
    }
    

    private fun hideAboutDialog() {
        uiState = uiState.copy(showAboutDialog = false)
    }
    

    private fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }
    

    private fun navigateBack() {

    }
    

    private fun navigateToStatistics() {

    }
}