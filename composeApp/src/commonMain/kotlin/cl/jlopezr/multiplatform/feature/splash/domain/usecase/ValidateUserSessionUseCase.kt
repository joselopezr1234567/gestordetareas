package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

alidateUserSessionUseCase(
    private val loginRepository: LoginRepository
) {
    

    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return loginRepository.isUserLoggedIn()
    }
}