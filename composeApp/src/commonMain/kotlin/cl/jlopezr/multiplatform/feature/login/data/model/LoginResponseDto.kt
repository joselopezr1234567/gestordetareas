package cl.jlopezr.multiplatform.feature.login.data.model

/**
 * DTO para la respuesta del login (sin tokens)
 */
data class LoginResponseDto(
    val success: Boolean,
    val user: UserDto?,
    val message: String?
)

data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String?
)