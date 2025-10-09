package cl.jlopezr.multiplatform.feature.login.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow


class IsUserLoggedInUseCase(
    private val repository: LoginRepository
) {
    

    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.isUserLoggedIn()
    }
}