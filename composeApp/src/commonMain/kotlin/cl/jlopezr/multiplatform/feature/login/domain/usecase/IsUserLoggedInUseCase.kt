package cl.jlopezr.multiplatform.feature.login.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para verificar si el usuario está logueado
 */
class IsUserLoggedInUseCase(
    private val repository: LoginRepository
) {
    
    /**
     * Verifica si el usuario está logueado
     * @return Flow con el estado de la sesión
     */
    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.isUserLoggedIn()
    }
}