package cl.jlopezr.multiplatform.feature.login.data.model

/**
 * Data Transfer Object para la solicitud de login
 */
data class LoginRequestDto(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)