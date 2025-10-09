package cl.jlopezr.multiplatform.feature.splash.data.datasource.local

import cl.jlopezr.multiplatform.core.util.Constants
import cl.jlopezr.multiplatform.core.util.getCurrentTimeMillis


class SplashLocalDataSource {
    

    private var cachedToken: String? = null
    private var lastConfigUpdate: Long = 0L
    

    fun getSavedToken(): String? {

        return cachedToken
    }
    

    fun saveToken(token: String) {

        cachedToken = token
    }
    

    fun clearToken() {
        cachedToken = null
    }
    

    fun isCacheValid(): Boolean {
        val currentTime = getCurrentTimeMillis()
        val cacheAge = currentTime - lastConfigUpdate
        val maxCacheAge = Constants.SESSION_TIMEOUT_HOURS * 60 * 60 * 1000 // 24 horas en ms
        
        return cacheAge < maxCacheAge
    }
    

    fun updateConfigTimestamp() {
        lastConfigUpdate = getCurrentTimeMillis()
    }
    

    fun getCurrentAppVersion(): String {
        return Constants.APP_VERSION
    }
}