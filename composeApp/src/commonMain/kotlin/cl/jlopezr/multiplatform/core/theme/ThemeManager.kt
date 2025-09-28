package cl.jlopezr.multiplatform.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Enum para los tipos de tema disponibles
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

/**
 * Manager para manejar el estado del tema de la aplicación
 * Permite cambiar entre tema claro, oscuro y seguir el sistema
 */
class ThemeManager {
    
    private val _currentTheme = MutableStateFlow(ThemeMode.SYSTEM)
    val currentTheme: StateFlow<ThemeMode> = _currentTheme.asStateFlow()
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    /**
     * Cambia el tema actual
     */
    fun setTheme(theme: ThemeMode) {
        _currentTheme.value = theme
        updateDarkThemeState(theme)
    }
    
    /**
     * Alterna entre tema claro y oscuro
     */
    fun toggleTheme() {
        val newTheme = when (_currentTheme.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> if (_isDarkTheme.value) ThemeMode.LIGHT else ThemeMode.DARK
        }
        setTheme(newTheme)
    }
    
    /**
     * Actualiza el estado del tema oscuro basado en el modo seleccionado
     */
    private fun updateDarkThemeState(theme: ThemeMode) {
        _isDarkTheme.value = when (theme) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
    
    /**
     * Determina si el sistema está en modo oscuro
     * Esta función debe ser implementada específicamente para cada plataforma
     */
    private fun isSystemInDarkTheme(): Boolean {
        // Por defecto retorna false, se puede sobrescribir en implementaciones específicas
        return false
    }
    
    /**
     * Inicializa el tema con el valor guardado o el predeterminado
     */
    fun initializeTheme(savedTheme: ThemeMode? = null) {
        val theme = savedTheme ?: ThemeMode.SYSTEM
        setTheme(theme)
    }
}