package cl.jlopezr.multiplatform.feature.login.data.model


data class LoginRequestDto(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)