package cl.jlopezr.multiplatform.feature.splash.presentation

import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig


data class SplashUiState(
    val isLoading: Boolean = true,
    val splashConfig: SplashConfig? = null,
    val isSessionValid: Boolean? = null,
    val isVersionCompatible: Boolean? = null,
    val errorMessage: String? = null,
    val shouldNavigateToLogin: Boolean = false,
    val shouldNavigateToHome: Boolean = false,
    val shouldShowUpdateDialog: Boolean = false,
    val shouldShowMaintenanceScreen: Boolean = false,
    val loadingProgress: Float = 0f,
    val currentStep: SplashStep = SplashStep.LOADING_CONFIG
)


enum class SplashStep {
    LOADING_CONFIG,
    CHECKING_VERSION,
    VALIDATING_SESSION,
    COMPLETED,
    ERROR
}


sealed class SplashUiEvent {
    object RetryLoading : SplashUiEvent()
    object NavigateToLogin : SplashUiEvent()
    object NavigateToHome : SplashUiEvent()
    object DismissError : SplashUiEvent()
    object UpdateApp : SplashUiEvent()
}