package cl.jlopezr.multiplatform.feature.login.data.repository

import cl.jlopezr.multiplatform.core.network.ApiResponse
import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.data.datasource.local.LoginLocalDataSource
import cl.jlopezr.multiplatform.feature.login.data.datasource.remote.LoginRemoteDataSource
import cl.jlopezr.multiplatform.feature.login.data.model.LoginMapper.toDomain
import cl.jlopezr.multiplatform.feature.login.data.model.LoginRequestDto
import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class LoginRepositoryImpl(
    private val remoteDataSource: LoginRemoteDataSource,
    private val localDataSource: LoginLocalDataSource
) : LoginRepository {
    
    override suspend fun login(
        email: String, 
        password: String, 
        rememberMe: Boolean
    ): Flow<Resource<LoginResult>> = flow {
        try {
            emit(Resource.Loading())
            
            val request = LoginRequestDto(
                email = email,
                password = password,
                rememberMe = rememberMe
            )
            
            when (val response = remoteDataSource.login(request)) {
                is ApiResponse.Success -> {
                    val loginResult = response.data.toDomain()
                    
                    if (loginResult.isSuccessful()) {

                        localDataSource.saveUserEmail(email)
                        loginResult.user?.let { user ->
                            localDataSource.saveUserName(user.name)
                        }
                        localDataSource.saveRememberMe(rememberMe)
                        localDataSource.saveLoginState(true)
                        
                        emit(Resource.Success(loginResult))
                    } else {
                        emit(Resource.Error(loginResult.getErrorMessage()))
                    }
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.message))
                }
                is ApiResponse.Loading -> {
                    emit(Resource.Loading())
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }
    
    override suspend fun logout(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            
            when (val response = remoteDataSource.logout()) {
                is ApiResponse.Success -> {

                    localDataSource.clearSession()
                    emit(Resource.Success(true))
                }
                is ApiResponse.Error -> {

                    localDataSource.clearSession()
                    emit(Resource.Success(true))
                }
                is ApiResponse.Loading -> {
                    emit(Resource.Loading())
                }
            }
        } catch (e: Exception) {

            localDataSource.clearSession()
            emit(Resource.Success(true))
        }
    }
    
    override suspend fun isUserLoggedIn(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            
            val isLoggedIn = localDataSource.isLoggedIn()
            val hasEmail = !localDataSource.getUserEmail().isNullOrEmpty()
            
            val result = isLoggedIn && hasEmail
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Error al verificar sesión: ${e.message}"))
        }
    }
}