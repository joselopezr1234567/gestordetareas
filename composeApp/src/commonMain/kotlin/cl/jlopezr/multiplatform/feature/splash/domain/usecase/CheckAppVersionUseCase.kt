package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para verificar la versión de la aplicación
 * Determina si la versión actual es compatible con el servidor
 * Parte de la lógica de negocio del feature Splash
 */
class CheckAppVersionUseCase(
    private val repository: SplashRepository
) {
    
    /**
     * Ejecuta la verificación de versión
     * @return Flow<Resource<Boolean>> - true si la versión es compatible
     */
    operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.checkAppVersion()
    }
}