package cl.jlopezr.multiplatform.feature.login.domain.repository

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    

    suspend fun login(
        email: String, 
        password: String, 
        rememberMe: Boolean = false
    ): Flow<Resource<LoginResult>>
    

    suspend fun logout(): Flow<Resource<Boolean>>
    

    suspend fun isUserLoggedIn(): Flow<Resource<Boolean>>
}