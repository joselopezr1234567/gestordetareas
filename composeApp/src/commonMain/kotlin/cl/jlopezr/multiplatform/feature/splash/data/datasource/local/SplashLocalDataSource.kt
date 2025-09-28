package cl.jlopezr.multiplatform.feature.splash.data.datasource.local

import cl.jlopezr.multiplatform.core.util.Constants
import cl.jlopezr.multiplatform.core.util.getCurrentTimeMillis

/**
 * Data source local para el feature Splash
 * Maneja el almacenamiento local de tokens, preferencias y cache
 * En una implementación real, usaría SharedPreferences, DataStore, o SQLite
 */
class SplashLocalDataSource {
    
    // Simulación de almacenamiento local (en una app real sería persistente)
    private var cachedToken: String? = null
    private var lastConfigUpdate: Long = 0L
    
    /**
     * Obtiene el token de sesión guardado localmente
     */
    fun getSavedToken(): String? {
        // En una implementación real, esto vendría de SharedPreferences o DataStore
        return cachedToken
    }
    
    /**
     * Guarda el token de sesión localmente
     */
    fun saveToken(token: String) {
        // En una implementación real, esto se guardaría en SharedPreferences o DataStore
        cachedToken = token
    }
    
    /**
     * Elimina el token de sesión
     */
    fun clearToken() {
        cachedToken = null
    }
    
    /**
     * Verifica si el cache de configuración es válido
     */
    fun isCacheValid(): Boolean {
        val currentTime = getCurrentTimeMillis()
        val cacheAge = currentTime - lastConfigUpdate
        val maxCacheAge = Constants.SESSION_TIMEOUT_HOURS * 60 * 60 * 1000 // 24 horas en ms
        
        return cacheAge < maxCacheAge
    }
    
    /**
     * Actualiza el timestamp del último update de configuración
     */
    fun updateConfigTimestamp() {
        lastConfigUpdate = getCurrentTimeMillis()
    }
    
    /**
     * Obtiene la versión actual de la aplicación
     */
    fun getCurrentAppVersion(): String {
        return Constants.APP_VERSION
    }
}