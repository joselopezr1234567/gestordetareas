package cl.jlopezr.multiplatform.feature.splash.data.model


data class SplashConfigDto(
    val appName: String,
    val minVersion: String,
    val theme: String,
    val maintenanceMode: Boolean,
    val welcomeMessage: String,
    val serverTime: Long
)