package cl.jlopezr.multiplatform.feature.login.data.datasource.remote

import cl.jlopezr.multiplatform.core.network.ApiResponse
import cl.jlopezr.multiplatform.core.network.FakeApiRepository
import cl.jlopezr.multiplatform.feature.login.data.model.LoginRequestDto
import cl.jlopezr.multiplatform.feature.login.data.model.LoginResponseDto
import cl.jlopezr.multiplatform.feature.login.data.model.UserDto

/**
 * Data Source remoto para el Login
 * Encapsula las llamadas a la API para autenticación
 */
class LoginRemoteDataSource(
    private val apiRepository: FakeApiRepository
) {
    
    /**
     * Realiza el login del usuario
     */
    suspend fun login(request: LoginRequestDto): ApiResponse<LoginResponseDto> {
        return try {
            // Simulamos validación hardcodeada
            if (request.email == "jlopezr@lopez.cl" && request.password == "Ce1234567.") {
                val userDto = UserDto(
                    id = "user_001",
                    email = request.email,
                    name = "José López",
                    profilePicture = null
                )
                
                val response = LoginResponseDto(
                    success = true,
                    user = userDto,
                    message = "Login exitoso"
                )
                
                ApiResponse.Success(response)
            } else {
                val response = LoginResponseDto(
                    success = false,
                    user = null,
                    message = "Credenciales inválidas"
                )
                
                ApiResponse.Error("Credenciales inválidas", 401)
            }
        } catch (e: Exception) {
            ApiResponse.Error("Error de conexión: ${e.message}", null)
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    suspend fun logout(): ApiResponse<Boolean> {
        return try {
            // Simulamos logout exitoso
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error("Error al cerrar sesión: ${e.message}", null)
        }
    }
}