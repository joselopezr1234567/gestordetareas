package cl.jlopezr.multiplatform.feature.login.data.datasource.local

import cl.jlopezr.multiplatform.core.storage.PreferencesManager


class LoginLocalDataSource(
    private val preferencesManager: PreferencesManager
) {
    
    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    

    suspend fun saveUserEmail(email: String) {
        preferencesManager.putString(KEY_USER_EMAIL, email)
    }
    

    suspend fun getUserEmail(): String? {
        return preferencesManager.getString(KEY_USER_EMAIL)
    }
    

    suspend fun saveUserName(name: String) {
        preferencesManager.putString(KEY_USER_NAME, name)
    }
    

    suspend fun getUserName(): String? {
        return preferencesManager.getString(KEY_USER_NAME)
    }
    

    suspend fun saveRememberMe(remember: Boolean) {
        preferencesManager.putBoolean(KEY_REMEMBER_ME, remember)
    }
    

    suspend fun getRememberMe(): Boolean {
        return preferencesManager.getBoolean(KEY_REMEMBER_ME, false)
    }
    

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        preferencesManager.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }
    

    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    

    suspend fun clearSession() {
        preferencesManager.remove(KEY_USER_EMAIL)
        preferencesManager.remove(KEY_USER_NAME)
        preferencesManager.remove(KEY_REMEMBER_ME)
        preferencesManager.remove(KEY_IS_LOGGED_IN)
    }
}