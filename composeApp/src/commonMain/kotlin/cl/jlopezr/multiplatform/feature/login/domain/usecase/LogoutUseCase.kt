package cl.jlopezr.multiplatform.feature.login.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para realizar el logout del usuario
 */
class LogoutUseCase(
    private val repository: LoginRepository
) {
    
    /**
     * Ejecuta el logout del usuario
     * @return Flow con el resultado del logout
     */
    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.logout()
    }
}