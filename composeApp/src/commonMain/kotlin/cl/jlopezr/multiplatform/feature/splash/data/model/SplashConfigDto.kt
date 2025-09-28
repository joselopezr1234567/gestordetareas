package cl.jlopezr.multiplatform.feature.splash.data.model

/**
 * Data Transfer Object para la configuración del splash
 * Representa los datos que vienen del FakeApiRepository
 */
data class SplashConfigDto(
    val appName: String,
    val minVersion: String,
    val theme: String,
    val maintenanceMode: Boolean,
    val welcomeMessage: String,
    val serverTime: Long
)