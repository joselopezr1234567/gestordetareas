package cl.jlopezr.multiplatform.feature.settings.presentation.event

import cl.jlopezr.multiplatform.core.theme.ThemeMode


sealed class SettingsUiEvent {
    

    data object LoadSettings : SettingsUiEvent()
    data object SaveSettings : SettingsUiEvent()
    

    data class ChangeTheme(val theme: ThemeMode) : SettingsUiEvent()
    data object ShowThemeDialog : SettingsUiEvent()
    data object HideThemeDialog : SettingsUiEvent()
    

    data class ToggleNotifications(val enabled: Boolean) : SettingsUiEvent()
    

    data class ToggleAutoDelete(val enabled: Boolean) : SettingsUiEvent()
    data class ChangeAutoDeleteDays(val days: Int) : SettingsUiEvent()
    

    data class ChangeDefaultPriority(val priority: String) : SettingsUiEvent()
    

    data class ToggleShowCompleted(val show: Boolean) : SettingsUiEvent()
    

    data object ShowDeleteConfirmation : SettingsUiEvent()
    data object HideDeleteConfirmation : SettingsUiEvent()
    data object DeleteAllData : SettingsUiEvent()
    data object DeleteCompletedTasks : SettingsUiEvent()
    

    data object ShowAboutDialog : SettingsUiEvent()
    data object HideAboutDialog : SettingsUiEvent()
    

    data object ClearError : SettingsUiEvent()
    

    data object NavigateBack : SettingsUiEvent()
    data object NavigateToStatistics : SettingsUiEvent()
}