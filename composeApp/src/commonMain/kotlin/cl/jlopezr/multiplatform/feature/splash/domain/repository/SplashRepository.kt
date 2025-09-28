package cl.jlopezr.multiplatform.feature.splash.domain.repository

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio para el feature Splash
 * Define los contratos que debe cumplir la implementación en la capa de datos
 * Sigue el principio de inversión de dependencias de Clean Architecture
 */
interface SplashRepository {
    
    /**
     * Obtiene la configuración inicial de la aplicación
     * @return Flow con Resource que contiene SplashConfig
     */
    fun getSplashConfig(): Flow<Resource<SplashConfig>>
    
    /**
     * Valida la sesión actual del usuario
     * @return Flow con Resource que indica si la sesión es válida
     */
    fun validateUserSession(): Flow<Resource<Boolean>>
    
    /**
     * Verifica la versión actual de la aplicación
     * @return Flow con Resource que indica si la versión es compatible
     */
    fun checkAppVersion(): Flow<Resource<Boolean>>
}