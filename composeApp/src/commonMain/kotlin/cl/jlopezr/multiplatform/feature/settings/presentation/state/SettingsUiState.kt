package cl.jlopezr.multiplatform.feature.settings.presentation.state

import cl.jlopezr.multiplatform.core.theme.ThemeMode


data class SettingsUiState(
    val isLoading: Boolean = false,
    val currentTheme: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val autoDeleteEnabled: Boolean = false,
    val autoDeleteDays: Int = 30,
    val defaultPriority: String = "MEDIUM",
    val showCompletedTasks: Boolean = true,
    val errorMessage: String? = null,
    val showThemeDialog: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val showAboutDialog: Boolean = false
) {

    val currentThemeDisplayName: String
        get() = when (currentTheme) {
            ThemeMode.LIGHT -> "Claro"
            ThemeMode.DARK -> "Oscuro"
            ThemeMode.SYSTEM -> "Seguir sistema"
        }
    

    val priorityDisplayName: String
        get() = when (defaultPriority) {
            "HIGH" -> "Alta"
            "MEDIUM" -> "Media"
            "LOW" -> "Baja"
            else -> "Media"
        }
}