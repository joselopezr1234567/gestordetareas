package cl.jlopezr.multiplatform.feature.splash.data.datasource.remote

import cl.jlopezr.multiplatform.core.network.ApiResponse
import cl.jlopezr.multiplatform.core.network.FakeApiRepository
import cl.jlopezr.multiplatform.feature.splash.data.model.SplashConfigDto
import cl.jlopezr.multiplatform.feature.splash.data.model.VersionCheckDto

/**
 * Data source remoto para el feature Splash
 * Encapsula las llamadas al FakeApiRepository
 * Actúa como intermediario entre el repositorio y la fuente de datos externa
 */
class SplashRemoteDataSource(
    private val fakeApiRepository: FakeApiRepository
) {
    
    /**
     * Obtiene la configuración del splash desde el API fake
     */
    suspend fun getSplashConfig(): ApiResponse<SplashConfigDto> {
        return fakeApiRepository.getSplashConfig()
    }
    
    /**
     * Valida la sesión del usuario usando el API fake
     */
    suspend fun validateUserSession(token: String?): ApiResponse<Boolean> {
        return fakeApiRepository.validateUserSession(token)
    }
    
    /**
     * Verifica la versión de la aplicación usando el API fake
     */
    suspend fun checkAppVersion(currentVersion: String): ApiResponse<VersionCheckDto> {
        return fakeApiRepository.checkAppVersion(currentVersion)
    }
}