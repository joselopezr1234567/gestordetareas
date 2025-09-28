package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para validar la sesión actual del usuario
 * Determina si el usuario tiene una sesión válida y puede acceder directamente
 * Ahora usa LoginRepository para unificar el sistema de autenticación
 */
class ValidateUserSessionUseCase(
    private val loginRepository: LoginRepository
) {
    
    /**
     * Ejecuta la validación de sesión
     * @return Flow<Resource<Boolean>> - true si la sesión es válida
     */
    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return loginRepository.isUserLoggedIn()
    }
}