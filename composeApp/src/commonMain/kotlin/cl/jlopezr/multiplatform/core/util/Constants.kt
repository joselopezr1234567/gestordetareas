package cl.jlopezr.multiplatform.core.util

/**
 * Constantes globales de la aplicación
 */
object Constants {
    
    // Configuración de la aplicación
    const val APP_NAME = "MyApplication5"
    const val APP_VERSION = "1.0.0"
    const val MIN_SUPPORTED_VERSION = "1.0.0"
    
    // Delays para simulación de red (FakeApiRepository)
    const val NETWORK_DELAY_SHORT = 500L
    const val NETWORK_DELAY_MEDIUM = 1000L
    const val NETWORK_DELAY_LONG = 1500L
    
    // Configuración de sesión
    const val SESSION_TIMEOUT_HOURS = 24L
    const val TOKEN_KEY = "user_token"
    
    // Temas disponibles
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"
    
    // Códigos de error
    const val ERROR_NETWORK = 1001
    const val ERROR_UNAUTHORIZED = 1002
    const val ERROR_VERSION_OUTDATED = 1003
    const val ERROR_MAINTENANCE = 1004
}