package cl.jlopezr.multiplatform.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}


class ThemeManager {
    
    private val _currentTheme = MutableStateFlow(ThemeMode.SYSTEM)
    val currentTheme: StateFlow<ThemeMode> = _currentTheme.asStateFlow()
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    

    fun setTheme(theme: ThemeMode) {
        _currentTheme.value = theme
        updateDarkThemeState(theme)
    }
    

    fun toggleTheme() {
        val newTheme = when (_currentTheme.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> if (_isDarkTheme.value) ThemeMode.LIGHT else ThemeMode.DARK
        }
        setTheme(newTheme)
    }
    

    private fun updateDarkThemeState(theme: ThemeMode) {
        _isDarkTheme.value = when (theme) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
    

    private fun isSystemInDarkTheme(): Boolean {
        // Por defecto retorna false, se puede sobrescribir en implementaciones específicas
        return false
    }
    

    fun initializeTheme(savedTheme: ThemeMode? = null) {
        val theme = savedTheme ?: ThemeMode.SYSTEM
        setTheme(theme)
    }
}