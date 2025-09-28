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

/**
 * ViewModel para la pantalla de configuraciones
 * Maneja el estado y eventos de la configuración de la aplicación
 */
class SettingsViewModel(
    private val themeManager: ThemeManager,
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    
    var uiState by mutableStateOf(SettingsUiState())
        private set
    
    init {
        loadSettings()
    }
    
    /**
     * Maneja los eventos de UI
     */
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
    
    /**
     * Carga las configuraciones actuales
     */
    private fun loadSettings() {
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
    
    /**
     * Guarda las configuraciones
     */
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
    
    /**
     * Cambia el tema de la aplicación
     */
    private fun changeTheme(theme: ThemeMode) {
        themeManager.setTheme(theme)
        uiState = uiState.copy(
            currentTheme = theme,
            showThemeDialog = false
        )
    }
    
    /**
     * Muestra el diálogo de selección de tema
     */
    private fun showThemeDialog() {
        uiState = uiState.copy(showThemeDialog = true)
    }
    
    /**
     * Oculta el diálogo de selección de tema
     */
    private fun hideThemeDialog() {
        uiState = uiState.copy(showThemeDialog = false)
    }
    
    /**
     * Alterna las notificaciones
     */
    private fun toggleNotifications(enabled: Boolean) {
        uiState = uiState.copy(notificationsEnabled = enabled)
    }
    
    /**
     * Alterna la auto-eliminación de tareas completadas
     */
    private fun toggleAutoDelete(enabled: Boolean) {
        uiState = uiState.copy(autoDeleteEnabled = enabled)
    }
    
    /**
     * Cambia los días para auto-eliminación
     */
    private fun changeAutoDeleteDays(days: Int) {
        uiState = uiState.copy(autoDeleteDays = days)
    }
    
    /**
     * Cambia la prioridad por defecto
     */
    private fun changeDefaultPriority(priority: String) {
        uiState = uiState.copy(defaultPriority = priority)
    }
    
    /**
     * Alterna mostrar tareas completadas
     */
    private fun toggleShowCompleted(show: Boolean) {
        uiState = uiState.copy(showCompletedTasks = show)
    }
    
    /**
     * Muestra confirmación de eliminación
     */
    private fun showDeleteConfirmation() {
        uiState = uiState.copy(showDeleteConfirmation = true)
    }
    
    /**
     * Oculta confirmación de eliminación
     */
    private fun hideDeleteConfirmation() {
        uiState = uiState.copy(showDeleteConfirmation = false)
    }
    
    /**
     * Elimina todos los datos
     */
    private fun deleteAllData() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                
                // Aquí se implementaría la eliminación de todos los datos
                // Por ahora solo simulamos
                
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
    
    /**
     * Elimina tareas completadas
     */
    private fun deleteCompletedTasks() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                
                // Aquí se implementaría la eliminación de tareas completadas
                // Por ahora solo simulamos
                
                uiState = uiState.copy(isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error al eliminar tareas completadas: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Muestra el diálogo "Acerca de"
     */
    private fun showAboutDialog() {
        uiState = uiState.copy(showAboutDialog = true)
    }
    
    /**
     * Oculta el diálogo "Acerca de"
     */
    private fun hideAboutDialog() {
        uiState = uiState.copy(showAboutDialog = false)
    }
    
    /**
     * Limpia el error actual
     */
    private fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }
    
    /**
     * Navega hacia atrás
     */
    private fun navigateBack() {
        // Este evento será manejado por la UI para navegar
    }
    
    /**
     * Navega a estadísticas
     */
    private fun navigateToStatistics() {
        // Este evento será manejado por la UI para navegar
    }
}