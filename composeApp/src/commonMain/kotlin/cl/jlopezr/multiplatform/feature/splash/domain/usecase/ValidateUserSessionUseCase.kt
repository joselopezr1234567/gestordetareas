package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para validar la sesión actual del usuario
 * Determina si el usuario tiene una sesión válida y puede acceder directamente
 * Parte de la lógica de negocio del feature Splash
 */
class ValidateUserSessionUseCase(
    private val repository: SplashRepository
) {
    
    /**
     * Ejecuta la validación de sesión
     * @return Flow<Resource<Boolean>> - true si la sesión es válida
     */
    operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.validateUserSession()
    }
}