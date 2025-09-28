package cl.jlopezr.multiplatform.feature.login.data.datasource.local

import cl.jlopezr.multiplatform.core.storage.PreferencesManager

/**
 * Data Source local para el Login (sin tokens)
 * Maneja el almacenamiento local del estado de sesión
 */
class LoginLocalDataSource(
    private val preferencesManager: PreferencesManager
) {
    
    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Guarda el email del usuario
     */
    suspend fun saveUserEmail(email: String) {
        preferencesManager.putString(KEY_USER_EMAIL, email)
    }
    
    /**
     * Obtiene el email del usuario
     */
    suspend fun getUserEmail(): String? {
        return preferencesManager.getString(KEY_USER_EMAIL)
    }
    
    /**
     * Guarda el nombre del usuario
     */
    suspend fun saveUserName(name: String) {
        preferencesManager.putString(KEY_USER_NAME, name)
    }
    
    /**
     * Obtiene el nombre del usuario
     */
    suspend fun getUserName(): String? {
        return preferencesManager.getString(KEY_USER_NAME)
    }
    
    /**
     * Guarda el estado de "recordar sesión"
     */
    suspend fun saveRememberMe(remember: Boolean) {
        preferencesManager.putBoolean(KEY_REMEMBER_ME, remember)
    }
    
    /**
     * Obtiene el estado de "recordar sesión"
     */
    suspend fun getRememberMe(): Boolean {
        return preferencesManager.getBoolean(KEY_REMEMBER_ME, false)
    }
    
    /**
     * Guarda el estado de login
     */
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        preferencesManager.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }
    
    /**
     * Obtiene el estado de login
     */
    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Limpia todos los datos de sesión
     */
    suspend fun clearSession() {
        preferencesManager.remove(KEY_USER_EMAIL)
        preferencesManager.remove(KEY_USER_NAME)
        preferencesManager.remove(KEY_REMEMBER_ME)
        preferencesManager.remove(KEY_IS_LOGGED_IN)
    }
}