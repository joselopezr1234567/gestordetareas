package cl.jlopezr.multiplatform.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.usecase.LoginUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.ValidateEmailUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.email,
                    emailError = null
                )
                validateEmail(event.email)
            }
            
            is LoginUiEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = event.password,
                    passwordError = null
                )
                validatePassword(event.password)
            }
            
            is LoginUiEvent.RememberMeChanged -> {
                _uiState.value = _uiState.value.copy(
                    rememberMe = event.rememberMe
                )
            }
            
            LoginUiEvent.TogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    isPasswordVisible = !_uiState.value.isPasswordVisible
                )
            }
            
            LoginUiEvent.Login -> {
                performLogin()
            }
            
            LoginUiEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = null
                )
            }
            
            LoginUiEvent.NavigateToHome -> {
                // Este evento será manejado por la UI para navegar
            }
        }
    }


    private fun validateEmail(email: String) {
        if (email.isNotBlank()) {
            val result = validateEmailUseCase(email)
            if (!result.successful) {
                _uiState.value = _uiState.value.copy(
                    emailError = result.errorMessage
                )
            }
        }
    }


    private fun validatePassword(password: String) {
        if (password.isNotBlank()) {
            val result = validatePasswordUseCase(password)
            if (!result.successful) {
                _uiState.value = _uiState.value.copy(
                    passwordError = result.errorMessage
                )
            }
        }
    }


    private fun performLogin() {
        val currentState = _uiState.value
        

        val emailValidation = validateEmailUseCase(currentState.email)
        val passwordValidation = validatePasswordUseCase(currentState.password)
        
        if (!emailValidation.successful || !passwordValidation.successful) {
            _uiState.value = currentState.copy(
                emailError = if (!emailValidation.successful) emailValidation.errorMessage else null,
                passwordError = if (!passwordValidation.successful) passwordValidation.errorMessage else null
            )
            return
        }
        
        viewModelScope.launch {
            loginUseCase(
                email = currentState.email,
                password = currentState.password,
                rememberMe = currentState.rememberMe
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = currentState.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    
                    is Resource.Success -> {
                        val loginResult = resource.data
                        if (loginResult?.isSuccessful() == true) {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                isLoginSuccessful = true,
                                user = loginResult.user,
                                errorMessage = null
                            )
                        } else {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                errorMessage = loginResult?.getErrorMessage() ?: "Error desconocido"
                            )
                        }
                    }
                    
                    is Resource.Error -> {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = resource.message ?: "Error de conexión"
                        )
                    }
                }
            }
        }
    }
}