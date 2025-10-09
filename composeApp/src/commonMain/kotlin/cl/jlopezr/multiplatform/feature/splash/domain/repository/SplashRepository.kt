package cl.jlopezr.multiplatform.feature.splash.domain.repository

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig
import kotlinx.coroutines.flow.Flow


interface SplashRepository {
    

    fun getSplashConfig(): Flow<Resource<SplashConfig>>
    

    fun validateUserSession(): Flow<Resource<Boolean>>
    

    fun checkAppVersion(): Flow<Resource<Boolean>>
}