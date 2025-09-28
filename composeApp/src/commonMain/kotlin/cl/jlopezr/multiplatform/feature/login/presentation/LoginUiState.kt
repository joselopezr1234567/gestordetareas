package cl.jlopezr.multiplatform.feature.login.presentation

import cl.jlopezr.multiplatform.feature.login.domain.model.User

/**
 * Estados de UI para la pantalla de Login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false
) {
    val isFormValid: Boolean
        get() = email.isNotBlank() && 
                password.isNotBlank() && 
                emailError == null && 
                passwordError == null
}

/**
 * Eventos de UI para la pantalla de Login
 */
sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    data class RememberMeChanged(val rememberMe: Boolean) : LoginUiEvent()
    object TogglePasswordVisibility : LoginUiEvent()
    object Login : LoginUiEvent()
    object ClearError : LoginUiEvent()
    object NavigateToHome : LoginUiEvent()
}