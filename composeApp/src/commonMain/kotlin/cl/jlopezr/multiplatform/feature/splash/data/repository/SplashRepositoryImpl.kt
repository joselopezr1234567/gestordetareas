package cl.jlopezr.multiplatform.feature.splash.data.repository

import cl.jlopezr.multiplatform.core.network.ApiResponse
import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.data.datasource.local.SplashLocalDataSource
import cl.jlopezr.multiplatform.feature.splash.data.datasource.remote.SplashRemoteDataSource
import cl.jlopezr.multiplatform.feature.splash.data.model.SplashConfigMapper.toDomain
import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SplashRepositoryImpl(
    private val remoteDataSource: SplashRemoteDataSource,
    private val localDataSource: SplashLocalDataSource
) : SplashRepository {
    
    override fun getSplashConfig(): Flow<Resource<SplashConfig>> = flow {
        try {
            emit(Resource.Loading())
            

            when (val response = remoteDataSource.getSplashConfig()) {
                is ApiResponse.Success -> {
                    val domainModel = response.data.toDomain()
                    localDataSource.updateConfigTimestamp()
                    emit(Resource.Success(domainModel))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(
                        message = response.message,
                        data = null
                    ))
                }
                is ApiResponse.Loading -> {
                    emit(Resource.Loading())
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Error inesperado al cargar configuración: ${e.message}",
                data = null
            ))
        }
    }
    
    override fun validateUserSession(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            

            val savedToken = localDataSource.getSavedToken()
            

            when (val response = remoteDataSource.validateUserSession(savedToken)) {
                is ApiResponse.Success -> {
                    val isValid = response.data
                    if (!isValid) {

                        localDataSource.clearToken()
                    }
                    emit(Resource.Success(isValid))
                }
                is ApiResponse.Error -> {

                    localDataSource.clearToken()
                    emit(Resource.Error(
                        message = response.message,
                        data = false
                    ))
                }
                is ApiResponse.Loading -> {
                    emit(Resource.Loading())
                }
            }
        } catch (e: Exception) {
            localDataSource.clearToken()
            emit(Resource.Error(
                message = "Error al validar sesión: ${e.message}",
                data = false
            ))
        }
    }
    
    override fun checkAppVersion(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            
            val currentVersion = localDataSource.getCurrentAppVersion()
            
            when (val response = remoteDataSource.checkAppVersion(currentVersion)) {
                is ApiResponse.Success -> {
                    val versionCheck = response.data
                    val isCompatible = !versionCheck.isUpdateRequired
                    emit(Resource.Success(isCompatible))
                }
                is ApiResponse.Error -> {
                    // En caso de error, asumir que la versión es compatible
                    emit(Resource.Error(
                        message = response.message,
                        data = true // Permitir continuar si no se puede verificar
                    ))
                }
                is ApiResponse.Loading -> {
                    emit(Resource.Loading())
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Error al verificar versión: ${e.message}",
                data = true // Permitir continuar en caso de error
            ))
        }
    }
}