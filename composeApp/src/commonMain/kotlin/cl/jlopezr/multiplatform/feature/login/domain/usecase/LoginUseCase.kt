package cl.jlopezr.multiplatform.feature.login.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para realizar el login del usuario
 */
class LoginUseCase(
    private val repository: LoginRepository
) {
    
    /**
     * Ejecuta el login del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param rememberMe Si debe recordar la sesión
     * @return Flow con el resultado del login
     */
    suspend operator fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean = false
    ): Flow<Resource<LoginResult>> {
        return repository.login(email, password, rememberMe)
    }
}