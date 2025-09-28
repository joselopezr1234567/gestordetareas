package cl.jlopezr.multiplatform.feature.login.domain.repository

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio para el Login (sin tokens)
 * Define las operaciones de autenticación disponibles
 */
interface LoginRepository {
    
    /**
     * Realiza el login del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param rememberMe Si debe recordar la sesión
     * @return Flow con el resultado del login
     */
    suspend fun login(
        email: String, 
        password: String, 
        rememberMe: Boolean = false
    ): Flow<Resource<LoginResult>>
    
    /**
     * Cierra la sesión del usuario
     * @return Flow con el resultado del logout
     */
    suspend fun logout(): Flow<Resource<Boolean>>
    
    /**
     * Verifica si hay una sesión activa
     * @return Flow con el estado de la sesión
     */
    suspend fun isUserLoggedIn(): Flow<Resource<Boolean>>
}