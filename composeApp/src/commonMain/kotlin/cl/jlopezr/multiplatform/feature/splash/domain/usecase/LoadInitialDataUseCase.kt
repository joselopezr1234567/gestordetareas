package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow


class LoadInitialDataUseCase(
    private val repository: SplashRepository
) {
    

    operator fun invoke(): Flow<Resource<SplashConfig>> {
        return repository.getSplashConfig()
    }
}