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

/**
 * ViewModel para la pantalla Splash
 * Implementa el patrón MVI (Model-View-Intent)
 * Coordina los casos de uso y maneja el estado de la UI
 */
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
    
    /**
     * Maneja los eventos de UI
     */
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
    
    /**
     * Inicia la secuencia completa del splash
     */
    private fun startSplashSequence() {
        viewModelScope.launch {
            try {
                resetState()
                
                // Paso 1: Cargar configuración inicial
                loadInitialConfiguration()
                
                // Paso 2: Verificar versión de la aplicación
                checkAppVersion()
                
                // Paso 3: Validar sesión del usuario
                validateUserSession()
                
                // Paso 4: Completar y navegar
                completeSequence()
                
            } catch (e: Exception) {
                handleError("Error inesperado durante la inicialización: ${e.message}")
            }
        }
    }
    
    /**
     * Resetea el estado para un nuevo intento
     */
    private fun resetState() {
        _uiState.value = SplashUiState(
            isLoading = true,
            currentStep = SplashStep.LOADING_CONFIG,
            loadingProgress = 0f
        )
    }
    
    /**
     * Carga la configuración inicial
     */
    private suspend fun loadInitialConfiguration() {
        updateStep(SplashStep.LOADING_CONFIG, 0.2f)
        
        loadInitialDataUseCase()
            .catch { e -> 
                handleError("Error al cargar configuración: ${e.message}")
            }
            .collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Mantener estado de carga
                    }
                    is Resource.Success -> {
                        val config = resource.data
                        _uiState.value = _uiState.value.copy(
                            splashConfig = config,
                            shouldShowMaintenanceScreen = config?.isInMaintenanceMode() == true
                        )
                        
                        // Si está en mantenimiento, detener aquí
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
    
    /**
     * Verifica la versión de la aplicación
     */
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
                        // Mantener estado de carga
                    }
                    is Resource.Success -> {
                        val isCompatible = resource.data ?: true
                        _uiState.value = _uiState.value.copy(
                            isVersionCompatible = isCompatible,
                            shouldShowUpdateDialog = !isCompatible
                        )
                        
                        // Si la versión no es compatible, detener aquí
                        if (!isCompatible) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                currentStep = SplashStep.COMPLETED
                            )
                            return@collect
                        }
                    }
                    is Resource.Error -> {
                        // En caso de error, asumir versión compatible y continuar
                        _uiState.value = _uiState.value.copy(
                            isVersionCompatible = true
                        )
                    }
                }
            }
    }
    
    /**
     * Valida la sesión del usuario
     */
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
    
    /**
     * Completa la secuencia y determina la navegación
     */
    private suspend fun completeSequence() {
        if (_uiState.value.shouldShowMaintenanceScreen || 
            _uiState.value.shouldShowUpdateDialog) return
        
        updateStep(SplashStep.COMPLETED, 1.0f)
        
        // Pequeña pausa para mostrar el progreso completo
        delay(500)
        
        // Determinar navegación basada en el estado de la sesión
        val shouldGoToHome = _uiState.value.isSessionValid == true
        
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            shouldNavigateToHome = shouldGoToHome,
            shouldNavigateToLogin = !shouldGoToHome
        )
    }
    
    /**
     * Actualiza el paso actual y el progreso
     */
    private suspend fun updateStep(step: SplashStep, progress: Float) {
        _uiState.value = _uiState.value.copy(
            currentStep = step,
            loadingProgress = progress
        )
        // Pequeña pausa para visualizar el progreso
        delay(300)
    }
    
    /**
     * Maneja errores durante el proceso
     */
    private fun handleError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message,
            currentStep = SplashStep.ERROR
        )
    }
}