package cl.jlopezr.multiplatform.feature.settings.presentation.event

import cl.jlopezr.multiplatform.core.theme.ThemeMode

/**
 * Eventos UI para la pantalla de configuraciones
 */
sealed class SettingsUiEvent {
    
    // Eventos de carga
    data object LoadSettings : SettingsUiEvent()
    data object SaveSettings : SettingsUiEvent()
    
    // Eventos de tema
    data class ChangeTheme(val theme: ThemeMode) : SettingsUiEvent()
    data object ShowThemeDialog : SettingsUiEvent()
    data object HideThemeDialog : SettingsUiEvent()
    
    // Eventos de notificaciones
    data class ToggleNotifications(val enabled: Boolean) : SettingsUiEvent()
    
    // Eventos de auto-eliminación
    data class ToggleAutoDelete(val enabled: Boolean) : SettingsUiEvent()
    data class ChangeAutoDeleteDays(val days: Int) : SettingsUiEvent()
    
    // Eventos de prioridad por defecto
    data class ChangeDefaultPriority(val priority: String) : SettingsUiEvent()
    
    // Eventos de visualización
    data class ToggleShowCompleted(val show: Boolean) : SettingsUiEvent()
    
    // Eventos de datos
    data object ShowDeleteConfirmation : SettingsUiEvent()
    data object HideDeleteConfirmation : SettingsUiEvent()
    data object DeleteAllData : SettingsUiEvent()
    data object DeleteCompletedTasks : SettingsUiEvent()
    
    // Eventos de información
    data object ShowAboutDialog : SettingsUiEvent()
    data object HideAboutDialog : SettingsUiEvent()
    
    // Eventos de error
    data object ClearError : SettingsUiEvent()
    
    // Eventos de navegación
    data object NavigateBack : SettingsUiEvent()
    data object NavigateToStatistics : SettingsUiEvent()
}