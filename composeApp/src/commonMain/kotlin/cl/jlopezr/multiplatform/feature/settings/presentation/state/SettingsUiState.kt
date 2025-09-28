package cl.jlopezr.multiplatform.feature.settings.presentation.state

import cl.jlopezr.multiplatform.core.theme.ThemeMode

/**
 * Estado UI para la pantalla de configuraciones
 */
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
    /**
     * Obtiene el nombre del tema actual para mostrar en la UI
     */
    val currentThemeDisplayName: String
        get() = when (currentTheme) {
            ThemeMode.LIGHT -> "Claro"
            ThemeMode.DARK -> "Oscuro"
            ThemeMode.SYSTEM -> "Seguir sistema"
        }
    
    /**
     * Obtiene el nombre de la prioridad por defecto para mostrar en la UI
     */
    val priorityDisplayName: String
        get() = when (defaultPriority) {
            "HIGH" -> "Alta"
            "MEDIUM" -> "Media"
            "LOW" -> "Baja"
            else -> "Media"
        }
}