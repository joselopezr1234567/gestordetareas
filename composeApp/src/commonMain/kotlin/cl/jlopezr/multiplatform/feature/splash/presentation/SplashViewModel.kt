package cl.jlopezr.multiplatform.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.CheckAppVersionUseCase
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.LoadInitialDataUseCase
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.ValidateUserSessionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class SplashViewModel(
    private val loadInitialDataUseCase: LoadInitialDataUseCase,
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val validateUserSessionUseCase: ValidateUserSessionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()
    
    init {
        startSplashSequence()
    }
    

    fun onEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.RetryLoading -> {
                startSplashSequence()
            }
            is SplashUiEvent.NavigateToLogin -> {
                _uiState.value = _uiState.value.copy(
                    shouldNavigateToLogin = true
                )
            }
            is SplashUiEvent.NavigateToHome -> {
                _uiState.value = _uiState.value.copy(
                    shouldNavigateToHome = true
                )
            }
            is SplashUiEvent.DismissError -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = null
                )
            }
            is SplashUiEvent.UpdateApp -> {
                // En una implementación real, abriría la tienda de aplicaciones
                _uiState.value = _uiState.value.copy(
                    shouldShowUpdateDialog = false
                )
            }
        }
    }
    

    private fun startSplashSequence() {
        viewModelScope.launch {
            try {
                resetState()
                

                loadInitialConfiguration()
                

                checkAppVersion()
                

                validateUserSession()
                

                completeSequence()
                
            } catch (e: Exception) {
                handleError("Error inesperado durante la inicialización: ${e.message}")
            }
        }
    }
    

    private fun resetState() {
        _uiState.value = SplashUiState(
            isLoading = true,
            currentStep = SplashStep.LOADING_CONFIG,
            loadingProgress = 0f
        )
    }
    

    private suspend fun loadInitialConfiguration() {
        updateStep(SplashStep.LOADING_CONFIG, 0.2f)
        
        loadInitialDataUseCase()
            .catch { e -> 
                handleError("Error al cargar configuración: ${e.message}")
            }
            .collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val config = resource.data
                        _uiState.value = _uiState.value.copy(
                            splashConfig = config,
                            shouldShowMaintenanceScreen = config?.isInMaintenanceMode() == true
                        )
                        

                        if (config?.isInMaintenanceMode() == true) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                currentStep = SplashStep.COMPLETED
                            )
                            return@collect
                        }
                    }
                    is Resource.Error -> {
                        handleError(resource.message ?: "Error desconocido al cargar configuración")
                        return@collect
                    }
                }
            }
    }
    

    private suspend fun checkAppVersion() {
        if (_uiState.value.shouldShowMaintenanceScreen) return
        
        updateStep(SplashStep.CHECKING_VERSION, 0.5f)
        
        checkAppVersionUseCase()
            .catch { e -> 
                handleError("Error al verificar versión: ${e.message}")
            }
            .collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val isCompatible = resource.data ?: true
                        _uiState.value = _uiState.value.copy(
                            isVersionCompatible = isCompatible,
                            shouldShowUpdateDialog = !isCompatible
                        )
                        

                        if (!isCompatible) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                currentStep = SplashStep.COMPLETED
                            )
                            return@collect
                        }
                    }
                    is Resource.Error -> {

                        _uiState.value = _uiState.value.copy(
                            isVersionCompatible = true
                        )
                    }
                }
            }
    }
    

    private suspend fun validateUserSession() {
        if (_uiState.value.shouldShowMaintenanceScreen || 
            _uiState.value.shouldShowUpdateDialog) return
        
        updateStep(SplashStep.VALIDATING_SESSION, 0.8f)
        
        validateUserSessionUseCase()
            .catch { e -> 
                handleError("Error al validar sesión: ${e.message}")
            }
            .collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Mantener estado de carga
                    }
                    is Resource.Success -> {
                        val isValid = resource.data ?: false
                        _uiState.value = _uiState.value.copy(
                            isSessionValid = isValid
                        )
                    }
                    is Resource.Error -> {
                        // En caso de error, asumir sesión inválida
                        _uiState.value = _uiState.value.copy(
                            isSessionValid = false
                        )
                    }
                }
            }
    }
    

    private suspend fun completeSequence() {
        if (_uiState.value.shouldShowMaintenanceScreen || 
            _uiState.value.shouldShowUpdateDialog) return
        
        updateStep(SplashStep.COMPLETED, 1.0f)
        

        delay(500)
        

        val shouldGoToHome = _uiState.value.isSessionValid == true
        
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            shouldNavigateToHome = shouldGoToHome,
            shouldNavigateToLogin = !shouldGoToHome
        )
    }
    

    private suspend fun updateStep(step: SplashStep, progress: Float) {
        _uiState.value = _uiState.value.copy(
            currentStep = step,
            loadingProgress = progress
        )
        // Pequeña pausa para visualizar el progreso
        delay(300)
    }
    

    private fun handleError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message,
            currentStep = SplashStep.ERROR
        )
    }
}