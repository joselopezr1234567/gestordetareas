package cl.jlopezr.multiplatform.core.network

import cl.jlopezr.multiplatform.core.util.Constants
import cl.jlopezr.multiplatform.core.util.getCurrentTimeMillis
import cl.jlopezr.multiplatform.feature.splash.data.model.SplashConfigDto
import cl.jlopezr.multiplatform.feature.splash.data.model.VersionCheckDto
import kotlinx.coroutines.delay


class FakeApiRepository {
    
    // ========== SPLASH FEATURE ==========
    

    suspend fun getSplashConfig(): ApiResponse<SplashConfigDto> {
        return try {
            delay(Constants.NETWORK_DELAY_LONG) // Simula delay de red
            
            val config = SplashConfigDto(
                appName = Constants.APP_NAME,
                minVersion = Constants.MIN_SUPPORTED_VERSION,
                theme = Constants.THEME_LIGHT,
                maintenanceMode = false,
                welcomeMessage = "¡Bienvenido a ${Constants.APP_NAME}!",
                serverTime = getCurrentTimeMillis()
            )
            
            ApiResponse.Success(config)
        } catch (e: Exception) {
            ApiResponse.Error("Error al cargar configuración inicial", Constants.ERROR_NETWORK, e)
        }
    }
    

    suspend fun validateUserSession(token: String?): ApiResponse<Boolean> {
        return try {
            delay(Constants.NETWORK_DELAY_MEDIUM)
            

            val isValid = when {
                token.isNullOrBlank() -> false
                token == "valid_token_123" -> true
                token.length < 10 -> false
                else -> true // Para testing, tokens largos son válidos
            }
            
            ApiResponse.Success(isValid)
        } catch (e: Exception) {
            ApiResponse.Error("Error al validar sesión", Constants.ERROR_UNAUTHORIZED, e)
        }
    }
    

    suspend fun checkAppVersion(currentVersion: String): ApiResponse<VersionCheckDto> {
        return try {
            delay(Constants.NETWORK_DELAY_SHORT)
            
            val versionCheck = VersionCheckDto(
                currentVersion = currentVersion,
                latestVersion = Constants.APP_VERSION,
                isUpdateRequired = false,
                isUpdateAvailable = false,
                updateUrl = "https://play.google.com/store/apps/details?id=cl.jlopezr.multiplatform",
                releaseNotes = "Versión inicial con Clean Architecture"
            )
            
            ApiResponse.Success(versionCheck)
        } catch (e: Exception) {
            ApiResponse.Error("Error al verificar versión", Constants.ERROR_VERSION_OUTDATED, e)
        }
    }
    

    suspend fun login(email: String, password: String): ApiResponse<String> {
        return try {
            delay(Constants.NETWORK_DELAY_MEDIUM)
            

            val isValidCredentials = email.contains("@") && password.length >= 6
            
            if (isValidCredentials) {
                ApiResponse.Success("Login exitoso")
            } else {
                ApiResponse.Error("Credenciales inválidas", Constants.ERROR_UNAUTHORIZED)
            }
        } catch (e: Exception) {
            ApiResponse.Error("Error en el login", Constants.ERROR_NETWORK, e)
        }
    }
    

    suspend fun getHomeData(userId: String): ApiResponse<String> {
        return try {
            delay(Constants.NETWORK_DELAY_MEDIUM)
            
            val homeData = "Datos del home para usuario: $userId"
            ApiResponse.Success(homeData)
        } catch (e: Exception) {
            ApiResponse.Error("Error al cargar datos del home", Constants.ERROR_NETWORK, e)
        }
    }
}