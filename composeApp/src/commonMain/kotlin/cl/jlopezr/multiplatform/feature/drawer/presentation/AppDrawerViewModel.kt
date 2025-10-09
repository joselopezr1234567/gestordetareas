package cl.jlopezr.multiplatform.feature.drawer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.login.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AppDrawerViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppDrawerUiState())
    val uiState: StateFlow<AppDrawerUiState> = _uiState.asStateFlow()
    

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoggingOut = true)
            
            logoutUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoggingOut = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoggingOut = false,
                            logoutSuccess = true
                        )
                        onLogoutComplete()
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoggingOut = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }
    

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}


data class AppDrawerUiState(
    val isLoggingOut: Boolean = false,
    val logoutSuccess: Boolean = false,
    val errorMessage: String? = null
)