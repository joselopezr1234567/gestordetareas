package cl.jlopezr.multiplatform.feature.login.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow


class LoginUseCase(
    private val repository: LoginRepository
) {
    

    suspend operator fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean = false
    ): Flow<Resource<LoginResult>> {
        return repository.login(email, password, rememberMe)
    }
}